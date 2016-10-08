package fr.badblock.game.core18R3.fakeentities;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.fakeentities.FakeEntity.Visibility;
import fr.badblock.gameapi.packets.out.play.PlayEntityDestroy;
import fr.badblock.gameapi.players.BadblockPlayer;

public class FakeEntityTracker {
	public static final double TRACKING_RANGE = 144.0d;

	private World			              trackedWorld;
	private List<FakeEntityTrackerEntry>  trackedEntities;

	private Queue<FakeEntityTrackerEntry> toRemove, toAdd;
	
	public FakeEntityTracker(World world){
		this.trackedWorld	 = world;
		this.trackedEntities = Lists.newCopyOnWriteArrayList();
		this.toAdd  	     = Queues.newLinkedBlockingDeque();
		this.toRemove	     = Queues.newLinkedBlockingDeque();
	}
	
	public void addEntry(FakeEntityTrackerEntry entry){
		toAdd.add(entry);
	}
	
	public void removeEntry(FakeEntityTrackerEntry entry){
		toRemove.remove(entry);
	}
	
	public void doTick(){
		FakeEntityTrackerEntry entry;
		
		while((entry = toAdd.poll()) != null){
			trackedEntities.add(entry);
		}
		
		while((entry = toRemove.poll()) != null){
			trackedEntities.remove(entry);
		}
		
		Set<BadblockPlayer> players = trackedWorld.getPlayers().stream().map(player -> { return (BadblockPlayer) player; }).collect(Collectors.toSet());
		trackedEntities.stream().filter( entity -> { return entity.update(players); } ).forEach(entity -> trackedEntities.remove(entity));
	}
	
	static class FakeEntityTrackerEntry {
		GameFakeEntity<?>    fakeEntity;
		List<BadblockPlayer> players	 = Lists.newCopyOnWriteArrayList();

		public FakeEntityTrackerEntry(GameFakeEntity<?> entity){
			this.fakeEntity = entity;
		}
		
		/**
		 * @param players update
		 * @return if the entry must be deleted
		 */
		boolean update(Collection<BadblockPlayer> players){
			if(fakeEntity == null || fakeEntity.isRemoved()){
				remove();
				return true;
			}

			for(BadblockPlayer player : players)
				updatePlayer(player);
			
			return false;
		}

		void updatePlayer(BadblockPlayer player){
			if(player == null || fakeEntity.isRemoved()) return;

			boolean canSee = fakeEntity.getVisibility() == Visibility.SERVER || fakeEntity.see(player);
			
			if(!player.isOnline()){
				if(players.contains(player)){
					players.remove(player);
				}

				return;
			}

			if(!canSee){
				if(players.contains(player)){
					remove(player);
				}

				return;
			}
			
			if(isVisibleFrom(player.getLocation())) {
				if(!players.contains(player)){
					spawn(player);
				}
			} else if(players.contains(player)) {
				remove(player);
			}
		}

		void spawn(BadblockPlayer player){
			players.add(player);
			
			new BukkitRunnable(){
				private int count = 5;
				
				@Override
				public void run(){
					if(!players.contains(player) || !isVisibleFrom(player.getLocation())){
						cancel();
						return;
					}
					
					count--;
					fakeEntity.show0(player);					
					if(count == 0) cancel();
				}
			}.runTaskTimer(GameAPI.getAPI(), 0, 10L);
		}
		
		void remove(BadblockPlayer player){
			players.remove(player);
			
			GameAPI.getAPI().createPacket(PlayEntityDestroy.class).setEntities(new int[]{fakeEntity.getId()}).send(player);
		}
		
		void remove(){
			players.forEach(player -> remove(player));
		}

		boolean isVisibleFrom(Location location) {
			double dX = Math.abs(location.getX() - fakeEntity.getLocation().getX());
			double dZ = Math.abs(location.getZ() - fakeEntity.getLocation().getZ());

			return dX <= TRACKING_RANGE
					&& dZ <= TRACKING_RANGE
					&& location.getWorld().getName().equals(fakeEntity.getLocation().getWorld().getName());
		}
	}
}
package fr.badblock.game.v1_8_R3.fakeentities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.common.collect.Lists;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.packets.out.play.PlayEntityDestroy;
import fr.badblock.gameapi.players.BadblockPlayer;

public class FakeEntityTracker {
	public static final double TRACKING_RANGE = 144.0d;

	private World			             trackedWorld;
	private List<FakeEntityTrackerEntry> trackedEntities;

	public FakeEntityTracker(World world){
		this.trackedWorld	 = world;
		this.trackedEntities = Lists.newCopyOnWriteArrayList();
	}
	
	public void addEntry(FakeEntityTrackerEntry entry){
		trackedEntities.add(entry);
	}
	
	public void removeEntry(FakeEntityTrackerEntry entry){
		trackedEntities.remove(entry);
	}
	
	public void doTick(){
		Set<BadblockPlayer> players = trackedWorld.getPlayers().stream().map(player -> { return (BadblockPlayer) player; }).collect(Collectors.toSet());
		trackedEntities.stream().filter( entity -> { return entity.update(players); } ).forEach(entity -> trackedEntities.remove(entity));
	}
	
	static class FakeEntityTrackerEntry {
		GameFakeEntity<?>    fakeEntity;
		List<BadblockPlayer> players;

		public FakeEntityTrackerEntry(GameFakeEntity<?> entity){
			this.fakeEntity = entity;
			this.players	= new ArrayList<>();
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

			if(!player.isOnline()){
				if(players.contains(player)){
					players.remove(player);
				}

				return;
			}

			if(!fakeEntity.see(player)){
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
		
			fakeEntity.show0(player);
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
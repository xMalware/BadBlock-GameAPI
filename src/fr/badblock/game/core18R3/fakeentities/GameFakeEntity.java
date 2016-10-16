package fr.badblock.game.core18R3.fakeentities;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.badblock.game.core18R3.fakeentities.FakeEntityTracker.FakeEntityTrackerEntry;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityDestroy;
import fr.badblock.gameapi.packets.out.play.PlayEntityEquipment;
import fr.badblock.gameapi.packets.out.play.PlayEntityHeadRotation;
import fr.badblock.gameapi.packets.out.play.PlayEntityLook;
import fr.badblock.gameapi.packets.out.play.PlayEntityMetadata;
import fr.badblock.gameapi.packets.out.play.PlayEntityRelativeMove;
import fr.badblock.gameapi.packets.out.play.PlayEntityStatus;
import fr.badblock.gameapi.packets.out.play.PlayEntityStatus.EntityStatus;
import fr.badblock.gameapi.packets.out.play.PlayEntityTeleport;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;

public abstract class GameFakeEntity<T extends WatcherEntity> implements FakeEntity<T> {
	@Getter private final int 		 id;
	@Getter private final EntityType type;
	
	@Getter private Location   location;
	@Getter private float      headYaw;
	
	@Getter private List<UUID> whitelist, blacklist;
	@Getter private final T	   watchers;
	
	@Getter private boolean    removed;
	
	@Getter private FakeEntityTrackerEntry entry;
	
	@Getter
	private Visibility visibility = Visibility.SERVER;
	
	private final Map<EquipmentSlot, ItemStack> equipment;
	
	public GameFakeEntity(EntityType type, int entityId, T watchers, Location location){
		this.type     		= type;
		this.id 			= entityId;
		this.watchers 		= watchers;
		
		this.equipment		= Maps.newConcurrentMap();
		
		this.removed  	    = false;
		
		this.whitelist	    = Lists.newArrayList();
		this.blacklist      = Lists.newArrayList();
		
		this.location		= location;
		this.headYaw		= location.getYaw();
	
		this.entry		    = new FakeEntityTrackerEntry(this);
	}
	
	public void show0(BadblockPlayer player){
		getSpawnPackets().forEach(packet-> packet.send(player));
		GameAPI.getAPI().createPacket(PlayEntityMetadata.class)
						.setEntityId(id)
						.setWatcher(watchers)
						.send(player);
		
		if(headYaw != 0)
			GameAPI.getAPI().createPacket(PlayEntityHeadRotation.class)
							.setEntityId(id)
							.setRotation(headYaw)
							.send(player);
		
		for(EquipmentSlot slot : EquipmentSlot.values()){
			if(equipment.containsKey(slot)){
				GameAPI.getAPI().createPacket(PlayEntityEquipment.class)
								.setEntityId(id)
								.setSlot(slot.ordinal())
								.setItemStack(equipment.get(slot))
								.send(player);
			}
		}
	}
	
	@Override
	public void move(Location location) {
		Location oldLoc = this.location.clone();
		double dX = Math.abs(oldLoc.getX() - location.getX());
		double dY = Math.abs(oldLoc.getY() - location.getY());
		double dZ = Math.abs(oldLoc.getZ() - location.getZ());

		if (dX < 4 && dY < 4 && dZ < 4) {
			broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityRelativeMove.class)
											.setEntityId(id)
											.setMove(new Vector(location.getX() - oldLoc.getX(), location.getY() - oldLoc.getY(), location.getZ() - oldLoc.getZ()))
											.setOnGround(true));
			yaw(location);
		} else teleport(location);
		
		this.location = location;
	}
	
	protected void yaw(Location location){
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityLook.class)
										.setEntityId(id)
										.setPitch(location.getPitch())
										.setYaw(location.getYaw()));
		setHeadYaw(0);
	}

	@Override
	public void teleport(Location location) {
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityTeleport.class)
										.setEntityId(id)
										.setTo(location)
										.setOnGround(true));
		yaw(location);
		
		this.location = location;
	}

	@Override
	public void setHeadYaw(float yaw) {
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityHeadRotation.class)
										.setEntityId(id)
										.setRotation(yaw));
		this.headYaw = yaw;
	}
	
	@Override
	public void playStatus(EntityStatus status){
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityStatus.class)
										.setEntityId(id)
										.setStatus(status));
	}

	@Override
	public void updateWatchers() {
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityMetadata.class)
										.setEntityId(id)
										.setWatcher(watchers));
	}

	@Override
	public void setEquipment(EquipmentSlot equipmentSlot, ItemStack itemstack) {
		equipment.put(equipmentSlot, itemstack);
		
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityEquipment.class)
										.setEntityId(id)
										.setSlot(equipmentSlot.ordinal())
										.setItemStack(itemstack));
	}

	@Override
	public void kill() {
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityStatus.class)
										.setEntityId(id)
										.setStatus(EntityStatus.ENTITY_DEAD));
		remove();
	}

	@Override
	public void remove() {
		whitelist.clear();
	}
	
	@Override
	public void remove(BadblockPlayer player) {
		whitelist.remove(player.getUniqueId());
	}
	
	public void remove0(BadblockPlayer player){
		GameAPI.getAPI().createPacket(PlayEntityDestroy.class).setEntities(new int[]{getId()}).send(player);
	}

	@Override
	public void destroy(){
		remove();
		
		FakeEntities.destroy(this);
		this.removed = true;
	}

	public abstract List<BadblockOutPacket> getSpawnPackets();
	
	public void broadcastPacket(BadblockOutPacket packet){
		for(BadblockPlayer player : entry.players){
			player.sendPacket(packet);
		}
		
		entry.players.forEach(player -> player.sendPacket(packet));
	}
	
	@Override
	public void addPlayer(EntityViewList list, BadblockPlayer player) {
		if(!removed && !isPlayerIn(list, player)) getList(list).add(player.getUniqueId());
	}

	@Override
	public void removePlayer(EntityViewList list, BadblockPlayer player) {
		getList(list).remove(player.getUniqueId());		
	}

	@Override
	public boolean isPlayerIn(EntityViewList list, BadblockPlayer player) {
		return getList(list).contains(player.getUniqueId());
	}
	
	@Override
	public FakeEntity<?> setVisibility(Visibility visibility) {
		this.visibility = visibility;
		return this;
	}
	
	private List<UUID> getList(EntityViewList list){
		switch(list){
			case BLACKLIST: return blacklist;
			case WHITELIST: return whitelist;
		}
		
		return null;
	}
}

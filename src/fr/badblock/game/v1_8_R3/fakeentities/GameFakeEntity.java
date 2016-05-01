package fr.badblock.game.v1_8_R3.fakeentities;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityDestroy;
import fr.badblock.gameapi.packets.out.play.PlayEntityEquipment;
import fr.badblock.gameapi.packets.out.play.PlayEntityHeadRotation;
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
	
	@Getter private List<UUID> viewingPlayers;
	@Getter private final T	   watchers;
	
	@Getter private boolean    removed;
	
	private final Map<EquipmentSlot, ItemStack> equipment;
	
	public GameFakeEntity(EntityType type, int entityId, T watchers, Location location){
		this.type     		= type;
		this.id 			= entityId;
		this.watchers 		= watchers;
		
		this.equipment		= Maps.newConcurrentMap();
		
		this.removed  	    = false;
		this.viewingPlayers = Lists.newArrayList();
		
		this.location		= location;
		this.headYaw		= 0;
	}
	
	@Override
	public void show(BadblockPlayer player) {
		getSpawnPacket().send(player);
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
		
		viewingPlayers.add(player.getUniqueId());
	}


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
		} else teleport(location);
		
		this.location = location;
	}

	@Override
	public void teleport(Location location) {
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityTeleport.class)
										.setEntityId(id)
										.setTo(location)
										.setOnGround(true));
		
		this.location = location;
	}

	public void setHeadYaw(float yaw) {
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityHeadRotation.class)
										.setEntityId(id)
										.setRotation(yaw));
		this.headYaw = yaw;
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
		broadcastPacket(GameAPI.getAPI().createPacket(PlayEntityDestroy.class)
										.setEntities(new int[]{id}));
		
		viewingPlayers.clear();
	}

	public abstract BadblockOutPacket getSpawnPacket();
	
	public void broadcastPacket(BadblockOutPacket packet){
		for(UUID uniqueId : viewingPlayers){
			Player player = Bukkit.getPlayer(uniqueId);
			
			if(player != null){
				BadblockPlayer bPlayer = (BadblockPlayer) player;
				bPlayer.sendPacket(packet);
			}
		}
	}
}

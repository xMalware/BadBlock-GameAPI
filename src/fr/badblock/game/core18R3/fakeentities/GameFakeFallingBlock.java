package fr.badblock.game.core18R3.fakeentities;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityObject;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityObject.SpawnableObjects;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import lombok.NonNull;

public class GameFakeFallingBlock extends GameFakeEntity<WatcherEntity> {
	private Material material;
	private byte 	 data;
	
	public GameFakeFallingBlock(int entityId, Location location, @NonNull Material material, byte data) {
		super(EntityType.ARMOR_STAND, entityId, GameAPI.getAPI().createWatcher(WatcherEntity.class), location);
		this.material = material;
		this.data     = data;
	}

	@Override
	public List<BadblockOutPacket> getSpawnPackets() {
		return 
				Arrays.asList(
						GameAPI.getAPI().createPacket(PlaySpawnEntityObject.class)
							   .setEntityId(getId())
							   .setLocation(getLocation())
							   .setData(data())
							   .setType(SpawnableObjects.FALLING_OBJECT)
				);
	}
	
	@Override public void yaw(Location loc){}
	
	@SuppressWarnings("deprecation")
	private int data(){
		return material.getId() + (data << 12);
	}

}

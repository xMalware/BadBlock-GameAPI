package fr.badblock.game.core18R3.fakeentities;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityCreature;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;

public class GameFakeLivingEntity<T extends WatcherEntity> extends GameFakeEntity<T> {
	public GameFakeLivingEntity(EntityType type, int entityId, T watchers, Location location) {
		super(type, entityId, watchers, location);
	}

	@Override
	public List<BadblockOutPacket> getSpawnPackets() {
		return 
				Arrays.asList(
						GameAPI.getAPI().createPacket(PlaySpawnEntityCreature.class)
							   .setEntityId(getId())
							   .setLocation(getLocation())
							   .setHeadRotation(getHeadYaw())
							   .setType(getType())
							   .setWatchers(getWatchers())
				);
	}

}

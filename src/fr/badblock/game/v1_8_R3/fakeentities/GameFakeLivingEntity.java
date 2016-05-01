package fr.badblock.game.v1_8_R3.fakeentities;

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
	public BadblockOutPacket getSpawnPacket() {
		return GameAPI.getAPI().createPacket(PlaySpawnEntityCreature.class)
							   .setEntityId(getId())
							   .setLocation(getLocation())
							   .setHeadRotation(getHeadYaw())
							   .setType(getType())
							   .setWatchers(getWatchers());
	}

}

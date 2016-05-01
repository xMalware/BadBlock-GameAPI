package fr.badblock.game.v1_8_R3.fakeentities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;

public class FakeEntities {
	private static int lastId = -10;
	
	public static <T extends WatcherEntity> FakeEntity<T> spawnFakeLivingEntity(Location location, EntityType type, Class<T> clazz) {
		T watcher = GameAPI.getAPI().createWatcher(clazz);
		return new GameFakeLivingEntity<T>(type, lastId--, watcher, location);
	}

	public static FakeEntity<WatcherArmorStand> spawnFakeArmorStand(Location location) {
		return new GameFakeArmorStand(lastId--, location);
	}

}

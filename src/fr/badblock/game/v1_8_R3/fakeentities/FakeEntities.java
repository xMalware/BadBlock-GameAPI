package fr.badblock.game.v1_8_R3.fakeentities;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;

public class FakeEntities {
	private static int lastId = -10;
	private static final Map<Integer, GameFakeEntity<?>> entities = Maps.newConcurrentMap();
	
	public static <T extends WatcherEntity> FakeEntity<T> spawnFakeLivingEntity(Location location, EntityType type, Class<T> clazz) {
		T watcher = GameAPI.getAPI().createWatcher(clazz);
		return spawnNotify(new GameFakeLivingEntity<T>(type, lastId--, watcher, location));
	}

	public static FakeEntity<WatcherArmorStand> spawnFakeArmorStand(Location location) {
		return spawnNotify(new GameFakeArmorStand(lastId--, location));
	}
	
	private static <T extends WatcherEntity> FakeEntity<T> spawnNotify(GameFakeEntity<T> entity){
		entities.put(entity.getId(), entity);
		return entity;
	}
	
	protected static void destroy(FakeEntity<?> entity){
		entities.remove(entity.getId());
	}
	
	public static FakeEntity<?> retrieveFakeEntity(int id){
		return entities.get(id);
	}

}

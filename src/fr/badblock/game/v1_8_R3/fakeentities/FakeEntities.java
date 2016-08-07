package fr.badblock.game.v1_8_R3.fakeentities;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;

public class FakeEntities {
	private static int lastId = -10;
	private static final Map<Integer, GameFakeEntity<?>> entities = Maps.newConcurrentMap();
	
	private static Map<String, FakeEntityTracker> trackers = Maps.newConcurrentMap();
	
	private static FakeEntityTracker getTracker(World world){
		if(!trackers.containsKey(world.getName())){
			FakeEntityTracker tracker = new FakeEntityTracker(world);
			
			Bukkit.getScheduler().runTaskTimer(GameAPI.getAPI(), tracker::doTick, 1L, 1L);
			trackers.put(world.getName(), new FakeEntityTracker(world));
		}
		
		return trackers.get(world.getName());
	}
	
	public static <T extends WatcherEntity> FakeEntity<T> spawnFakeLivingEntity(Location location, EntityType type, Class<T> clazz) {
		T watcher = GameAPI.getAPI().createWatcher(clazz);
		return spawnNotify(new GameFakeLivingEntity<T>(type, lastId--, watcher, location));
	}

	public static FakeEntity<WatcherEntity> spawnFakeFallingBlock(Location location, Material type, byte data) {
		return spawnNotify(new GameFakeFallingBlock(lastId--, location, type, data));
	}
	
	public static FakeEntity<WatcherArmorStand> spawnFakeArmorStand(Location location) {
		return spawnNotify(new GameFakeArmorStand(lastId--, location));
	}
	
	private static <T extends WatcherEntity> FakeEntity<T> spawnNotify(GameFakeEntity<T> entity){
		entities.put(entity.getId(), entity);
		
		getTracker(entity.getLocation().getWorld()).addEntry(entity.getEntry());
		return entity;
	}
	
	protected static void destroy(GameFakeEntity<?> entity){
		entities.remove(entity.getId());
		getTracker(entity.getLocation().getWorld()).removeEntry(entity.getEntry());
	}
	
	public static FakeEntity<?> retrieveFakeEntity(int id){
		return entities.get(id);
	}

}

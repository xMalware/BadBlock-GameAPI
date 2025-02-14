package fr.badblock.game.core18R3.fakeentities;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.out.play.PlayPlayerInfo.PlayerInfo;
import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.packets.watchers.WatcherLivingEntity;

public class FakeEntities {
	private static int lastId = -10;
	private static final Map<Integer, GameFakeEntity<?>> entities = Maps.newConcurrentMap();
	
	private static Map<String, FakeEntityTracker> trackers = Maps.newConcurrentMap();
	
	private static FakeEntityTracker getTracker(World world){
		if(!trackers.containsKey(world.getName())){
			FakeEntityTracker tracker = new FakeEntityTracker(world);
			
			Bukkit.getScheduler().runTaskTimer(GameAPI.getAPI(), tracker::doTick, 1L, 20 * 5L);
			trackers.put(world.getName(), tracker);
		}
		
		return trackers.get(world.getName());
	}
	
	public static void logFakeEntities()
	{
		trackers.entrySet().stream().forEach( v -> {
				System.out.println("--------- " + v.getKey() + " ----------");
				v.getValue().getTrackedEntities().forEach(entity -> System.out.println(entity.fakeEntity));
			}
		);
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
	
	public static FakeEntity<WatcherLivingEntity> spawnFakePlayer(Location location, PlayerInfo infos) {
		return spawnNotify(new GameFakePlayer(lastId--, infos, GameAPI.getAPI().createWatcher(WatcherLivingEntity.class), location));
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

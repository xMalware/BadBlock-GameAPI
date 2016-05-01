package fr.badblock.game.v1_8_R3.watchers;

import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Horse;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

import fr.badblock.gameapi.packets.watchers.WatcherAgeable;
import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;
import fr.badblock.gameapi.packets.watchers.WatcherBlaze;
import fr.badblock.gameapi.packets.watchers.WatcherCreeper;
import fr.badblock.gameapi.packets.watchers.WatcherEnderman;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.packets.watchers.WatcherGhast;
import fr.badblock.gameapi.packets.watchers.WatcherHorse;
import fr.badblock.gameapi.packets.watchers.WatcherIronGolem;
import fr.badblock.gameapi.packets.watchers.WatcherLivingEntity;
import fr.badblock.gameapi.packets.watchers.WatcherOcelot;
import fr.badblock.gameapi.packets.watchers.WatcherPig;
import fr.badblock.gameapi.packets.watchers.WatcherRabbit;
import fr.badblock.gameapi.packets.watchers.WatcherSheep;
import fr.badblock.gameapi.packets.watchers.WatcherSkeleton;
import fr.badblock.gameapi.packets.watchers.WatcherSlime;
import fr.badblock.gameapi.packets.watchers.WatcherTameableAnimal;
import fr.badblock.gameapi.packets.watchers.WatcherVillager;
import fr.badblock.gameapi.packets.watchers.WatcherWitch;
import fr.badblock.gameapi.packets.watchers.WatcherWither;
import fr.badblock.gameapi.packets.watchers.WatcherWolf;
import fr.badblock.gameapi.packets.watchers.WatcherZombie;
import lombok.Getter;

public enum GameWatchers {
	AGEABLE(WatcherAgeable.class, GameWatcherAgeable.class, Ageable.class),
	ARMOR_STAND(WatcherArmorStand.class, GameWatcherArmorStand.class, ArmorStand.class),
	BLAZE(WatcherBlaze.class, GameWatcherBlaze.class, Blaze.class),
	CREEPER(WatcherCreeper.class, GameWatcherCreeper.class, Creeper.class),
	ENDERMAN(WatcherEnderman.class, GameWatcherEnderman.class, Enderman.class),
	ENTITY(WatcherEntity.class, GameWatcherEntity.class, Entity.class),
	GHAST(WatcherGhast.class, GameWatcherGhast.class, Ghast.class),
	HORSE(WatcherHorse.class, GameWatcherHorse.class, Horse.class),
	IRON_GOLEM(WatcherIronGolem.class, GameWatcherIronGolem.class, IronGolem.class),
	LIVING_ENTITY(WatcherLivingEntity.class, GameWatcherLivingEntity.class, LivingEntity.class),
	OCELOT(WatcherOcelot.class, GameWatcherOcelot.class, Ocelot.class),
	PIG(WatcherPig.class, GameWatcherPig.class, Pig.class),
	RABBIT(WatcherRabbit.class, GameWatcherRabbit.class, Rabbit.class),
	SHEEP(WatcherSheep.class, GameWatcherSheep.class, Sheep.class),
	SKELETON(WatcherSkeleton.class, GameWatcherSkeleton.class, Skeleton.class),
	SLIME(WatcherSlime.class, GameWatcherSlime.class, Slime.class),
	TAMEABLE_ANIMAL(WatcherTameableAnimal.class, GameWatcherTameableAnimal.class, Tameable.class),
	VILLAGER(WatcherVillager.class, GameWatcherVillager.class, Villager.class),
	WITCH(WatcherWitch.class, GameWatcherWitch.class, Witch.class),
	WITHER(WatcherWither.class, GameWatcherWither.class, Wither.class),
	WOLF(WatcherWolf.class, GameWatcherWolf.class, Wolf.class),
	ZOMBIE(WatcherZombie.class, GameWatcherZombie.class, Zombie.class);
	
	@Getter private final Class<? extends WatcherEntity> clazz;
	@Getter private final Class<? extends WatcherEntity> gameClazz;
	@Getter private final Class<?> 					     bukkitClazz;
	
	<T extends WatcherEntity> GameWatchers(Class<T> clazz, Class<? extends T> gameClazz, Class<?> bukkitClazz){
		this.clazz       = clazz;
		this.gameClazz   = gameClazz;
		this.bukkitClazz = bukkitClazz;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends WatcherEntity> T buildWatch(Class<T> classWatcher){
		for(GameWatchers watcher : values()){
			if(watcher.clazz.equals(classWatcher)){
				try {
					return (T) watcher.gameClazz.getConstructor(Entity.class.getClass()).newInstance(watcher.getBukkitClazz());
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
}

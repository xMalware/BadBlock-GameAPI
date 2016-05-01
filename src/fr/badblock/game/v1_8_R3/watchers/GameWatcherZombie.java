package fr.badblock.game.v1_8_R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherZombie;

public class GameWatcherZombie extends GameWatcherLivingEntity implements WatcherZombie {
	public GameWatcherZombie(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherZombie setBaby(boolean baby) {
		set(MetadataIndex.ZOMBIE_IS_CHILD, (byte) ((baby) ? 1 : 0));
		return this;
	}

	@Override
	public WatcherZombie setVillager(boolean villager) {
		set(MetadataIndex.ZOMBIE_IS_VILLAGER, (byte) ((villager) ? 1 : 0));
		return this;
	}

	@Override
	public WatcherZombie setConverting(boolean converting) {
		set(MetadataIndex.ZOMBIE_IS_CONVERTING, (byte) ((converting) ? 1 : 0));
		return this;
	}
}

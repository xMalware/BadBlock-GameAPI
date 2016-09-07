package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherSkeleton;

public class GameWatcherSkeleton extends GameWatcherLivingEntity implements WatcherSkeleton {
	public GameWatcherSkeleton(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherSkeleton setWither(boolean wither) {
		set(MetadataIndex.SKELETON_TYPE, wither ? (byte) 1 : (byte) 0);
		return this;
	}
}

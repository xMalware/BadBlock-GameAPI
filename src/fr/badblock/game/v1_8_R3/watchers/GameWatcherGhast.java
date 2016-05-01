package fr.badblock.game.v1_8_R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherGhast;

public class GameWatcherGhast extends GameWatcherLivingEntity implements WatcherGhast {
	public GameWatcherGhast(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherGhast setAttacking(boolean attacking) {
		set(MetadataIndex.GHAST_ATTACKING, attacking);
		return this;
	}
}

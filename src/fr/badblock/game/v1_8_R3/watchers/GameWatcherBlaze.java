package fr.badblock.game.v1_8_R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherBlaze;

public class GameWatcherBlaze extends GameWatcherLivingEntity implements WatcherBlaze {
	public GameWatcherBlaze(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherBlaze setOnFire(boolean onFire) {
		set(MetadataIndex.BLAZE_ON_FIRE, ((onFire) ? (byte) 1 : (byte) 0));
		return this;
	}

}

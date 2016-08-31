package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherPig;

public class GameWatcherPig extends GameWatcherAgeable implements WatcherPig {
	public GameWatcherPig(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherPig setSaddle(boolean saddle) {
		set(MetadataIndex.PIG_SADDLE, (saddle) ? (byte) 1 : (byte) 0);
		return this;
	}

}

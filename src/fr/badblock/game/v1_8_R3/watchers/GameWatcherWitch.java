package fr.badblock.game.v1_8_R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherWitch;

public class GameWatcherWitch extends GameWatcherLivingEntity implements WatcherWitch {
	public GameWatcherWitch(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherWitch setAggressive(boolean aggressive) {
		set(MetadataIndex.WITCH_AGGRESSIVE, (byte) (aggressive ? 1 : 0));
		return this;
	}
}

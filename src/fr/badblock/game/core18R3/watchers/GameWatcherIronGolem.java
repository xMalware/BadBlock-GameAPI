package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherIronGolem;

public class GameWatcherIronGolem extends GameWatcherLivingEntity implements WatcherIronGolem {
	public GameWatcherIronGolem(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherIronGolem setPlayerCreated(boolean playerCreated) {
		set(MetadataIndex.GOLEM_PLAYER_BUILT, (byte) (playerCreated ? 1 : 0));
		return this;
	}
}

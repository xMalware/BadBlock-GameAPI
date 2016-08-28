package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherAgeable;

public class GameWatcherAgeable extends GameWatcherLivingEntity implements WatcherAgeable {
	public GameWatcherAgeable(Class<? extends Entity> clazz) {
		super(clazz);
	}

	@Override
	public WatcherAgeable setBaby(boolean baby) {
		set(MetadataIndex.AGE, baby ? 1 : 0);
		return this;
	}
}

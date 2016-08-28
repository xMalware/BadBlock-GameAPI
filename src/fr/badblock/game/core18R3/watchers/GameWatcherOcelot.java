package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot.Type;

import fr.badblock.gameapi.packets.watchers.WatcherOcelot;

public class GameWatcherOcelot extends GameWatcherTameableAnimal implements WatcherOcelot {
	public GameWatcherOcelot(Class<? extends Entity> clazz) {
		super(clazz);
	}

	@Override
	public GameWatcherOcelot setType(Type type) {
		set(MetadataIndex.OCELOT_TYPE, (byte) type.ordinal());
		return this;
	}
}

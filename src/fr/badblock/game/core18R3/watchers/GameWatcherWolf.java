package fr.badblock.game.core18R3.watchers;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherWolf;

public class GameWatcherWolf extends GameWatcherTameableAnimal implements WatcherWolf {
	public GameWatcherWolf(Class<? extends Entity> clazz) {
		super(clazz);
	}

	@Override
	public WatcherWolf setAngry(boolean angry) {
		map.setBit(MetadataIndex.WOLF_FLAGS, 0x02, angry);
		return this;
	}

	@Override
	public WatcherWolf setBegging(boolean begging) {
		set(MetadataIndex.WOLF_BEGGING, (byte) ((begging) ? 1 : 0));
		return this;
	}

	@SuppressWarnings("deprecation")
	@Override
	public WatcherWolf setCollarColor(DyeColor color) {
		set(MetadataIndex.WOLF_COLOR, color.getDyeData());
		return this;
	}
}

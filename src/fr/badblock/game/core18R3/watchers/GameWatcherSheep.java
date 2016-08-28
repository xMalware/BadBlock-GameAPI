package fr.badblock.game.core18R3.watchers;

import org.bukkit.DyeColor;
import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherSheep;

public class GameWatcherSheep extends GameWatcherAgeable implements WatcherSheep {
	public GameWatcherSheep(Class<? extends Entity> clazz) {
		super(clazz);
	}

	@SuppressWarnings("deprecation")
	@Override
	public WatcherSheep setColor(DyeColor color) {
		byte theByte = (Byte) map.get(MetadataIndex.SHEEP_DATA);
		map.set(MetadataIndex.SHEEP_DATA, (byte) (theByte & 240 | color.getDyeData() & 15));
		return this;
	}

	@Override
	public WatcherSheep setSheared(boolean sheared) {
		map.setBit(MetadataIndex.SHEEP_DATA, 0x10, sheared);
		return this;
	}
}

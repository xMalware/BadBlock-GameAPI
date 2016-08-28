package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager.Profession;

import fr.badblock.gameapi.packets.watchers.WatcherVillager;

public class GameWatcherVillager extends GameWatcherAgeable implements WatcherVillager {
	public GameWatcherVillager(Class<? extends Entity> clazz) {
		super(clazz);
	}

	@Override
	public WatcherVillager setProfession(Profession profession) {
		set(MetadataIndex.VILLAGER_TYPE, profession.ordinal());
		return this;
	}
}

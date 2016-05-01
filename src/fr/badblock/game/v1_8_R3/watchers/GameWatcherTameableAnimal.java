package fr.badblock.game.v1_8_R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherTameableAnimal;

public class GameWatcherTameableAnimal extends GameWatcherAgeable implements WatcherTameableAnimal {
	public GameWatcherTameableAnimal(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherTameableAnimal setSitting(boolean sitting) {
		map.setBit(MetadataIndex.TAMEABLE_FLAGS, 0x01, sitting);
		return this;
	}

	@Override
	public WatcherTameableAnimal setTamed(boolean tamed) {
		map.setBit(MetadataIndex.TAMEABLE_FLAGS, 0x01, tamed);
		return this;
	}
}

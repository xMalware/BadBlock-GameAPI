package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherSlime;

public class GameWatcherSlime extends GameWatcherLivingEntity implements WatcherSlime {
	public GameWatcherSlime(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherSlime setSize(byte size) {
		set(MetadataIndex.SLIME_SIZE, size);
		return this;
	}
}

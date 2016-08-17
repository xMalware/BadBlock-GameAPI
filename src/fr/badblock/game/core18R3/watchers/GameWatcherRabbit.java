package fr.badblock.game.core18R3.watchers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Rabbit.Type;

import fr.badblock.gameapi.packets.watchers.WatcherRabbit;

public class GameWatcherRabbit extends GameWatcherAgeable implements WatcherRabbit {
	public GameWatcherRabbit(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherRabbit setType(Type type) {
		set(MetadataIndex.RABBIT_TYPE, (byte) type.ordinal());
		return this;
	}
}

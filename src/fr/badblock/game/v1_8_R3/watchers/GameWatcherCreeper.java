package fr.badblock.game.v1_8_R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherCreeper;

public class GameWatcherCreeper extends GameWatcherLivingEntity implements WatcherCreeper {
	public GameWatcherCreeper(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherCreeper setState(CreeperState state) {
		set(MetadataIndex.CREEPER_STATE, state.getValue());
		return this;
	}

	@Override
	public WatcherCreeper setPowered(boolean powered) {
		set(MetadataIndex.CREEPER_POWERED, (byte) ((powered) ? 1 : 0));
		return this;
	}
}

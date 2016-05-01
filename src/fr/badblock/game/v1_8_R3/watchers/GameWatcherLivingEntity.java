package fr.badblock.game.v1_8_R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherLivingEntity;

public class GameWatcherLivingEntity extends GameWatcherEntity implements WatcherLivingEntity {
	public GameWatcherLivingEntity(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherLivingEntity setArrowsInEntity(int arrows) {
		set(MetadataIndex.ARROW_COUNT, (byte) arrows);
		return this;
	}

}

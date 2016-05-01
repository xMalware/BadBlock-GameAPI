package fr.badblock.game.v1_8_R3.watchers;

import org.bukkit.entity.Entity;

import fr.badblock.gameapi.packets.watchers.WatcherWither;

public class GameWatcherWither extends GameWatcherLivingEntity implements WatcherWither {
	public GameWatcherWither(Class<? extends Entity> clazz){
		super(clazz);
	}
	
	@Override
	public WatcherWither setTargets(int e1, int e2, int e3) {
		set(MetadataIndex.WITHER_TARGET_1, e1);
		set(MetadataIndex.WITHER_TARGET_2, e2);
		set(MetadataIndex.WITHER_TARGET_3, e3);

		return this;
	}

	@Override
	public WatcherWither setInvulnerableTime(int time) {
		set(MetadataIndex.WITHER_INVULN_TIME, time);
		return this;
	}
}

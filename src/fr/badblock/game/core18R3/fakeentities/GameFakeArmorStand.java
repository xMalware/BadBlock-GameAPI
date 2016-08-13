package fr.badblock.game.core18R3.fakeentities;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;

public class GameFakeArmorStand extends GameFakeLivingEntity<WatcherArmorStand> {
	public GameFakeArmorStand(int entityId, Location location) {
		super(EntityType.ARMOR_STAND, entityId, GameAPI.getAPI().createWatcher(WatcherArmorStand.class), location);
	}
}

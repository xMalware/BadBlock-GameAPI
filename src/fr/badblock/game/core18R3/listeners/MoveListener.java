package fr.badblock.game.core18R3.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.players.ingamedata.CommandInGameData;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.portal.Portal;

public class MoveListener extends BadListener {
	@EventHandler
	public void onMove(PlayerMoveEvent e) {

		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();

		if (player.getCenterJail() != null) {

			Location loc = e.getTo().clone();
			loc.setY(player.getCenterJail().getY());

			if (loc.distance(player.getCenterJail()) >= player.getRadius()) {
				e.setCancelled(true);

				player.teleport(player.getCenterJail());
				player.sendTranslatedMessage("game.move-toofar");
			}

		} else if (player.isDisguised()) {
			player.getDisguiseEntity().teleport(e.getTo());
		}

		if (e.getFrom().getBlock().equals(e.getTo().getBlock()))
			return;

		Portal portal = GameAPI.getAPI().getPortal(e.getTo());

		if (portal != null && portal.canUsePortal(player, e.getTo())) {
			portal.teleport(player);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onTeleport(PlayerTeleportEvent e) {
		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();

		if (player.isDisguised()) {
			player.getDisguiseEntity().teleport(e.getTo());
		}
		player.inGameData(CommandInGameData.class).lastLocation = e.getFrom();
	}
}

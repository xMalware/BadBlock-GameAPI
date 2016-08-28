package fr.badblock.game.core18R3.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.BadListener;

public class ChangeWorldEvent extends BadListener {
	@EventHandler
	public void onTeleport(PlayerChangedWorldEvent e) {
		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();
		player.setCustomEnvironment(null);
	}
}

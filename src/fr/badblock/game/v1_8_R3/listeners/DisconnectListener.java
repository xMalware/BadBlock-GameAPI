package fr.badblock.game.v1_8_R3.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.api.PlayerReconnectionPropositionEvent;
import fr.badblock.gameapi.game.GameServer.WhileRunningConnectionTypes;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;

public class DisconnectListener extends BadListener {
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		if(GameAPI.getAPI().getGameServer().getGameState() != GameState.RUNNING) {
			if(player.getTeam() != null)
				player.getTeam().leaveTeam(player);
		} else {
			boolean backup = GamePlugin.getInstance().getGameServer().getType() == WhileRunningConnectionTypes.BACKUP;

			if(backup){
				PlayerReconnectionPropositionEvent event = new PlayerReconnectionPropositionEvent(player);
				Bukkit.getPluginManager().callEvent(event);
				backup = !event.isCancelled();
			}

			if(!backup){
				if(player.getTeam() != null){
					player.getTeam().leaveTeam(player);
				}

				return;
			}

			if(backup)
				GamePlugin.getInstance().getGameServer().remember(player);
		}
	}
}
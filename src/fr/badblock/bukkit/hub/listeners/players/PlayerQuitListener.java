package fr.badblock.bukkit.hub.listeners.players;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.selector.googleauth.AuthUtils;
import fr.badblock.bukkit.hub.listeners._HubListener;
import fr.badblock.bukkit.hub.listeners.vipzone.RaceListener;
import fr.badblock.bukkit.hub.listeners.vipzone.RaceListener.RaceState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class PlayerQuitListener extends _HubListener {

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		BadblockPlayer player = (BadblockPlayer) event.getPlayer();
		TaskManager.runTaskLater("hubPacketThread_quit_" + UUID.randomUUID().toString(), new Runnable() {
			@Override
			public void run() {
				BadBlockHub.getInstance().hubPacketThread.sendPacket();
			}
		}, 1);
		if (RaceListener.racePlayers.containsKey(player)) {
			if (RaceListener.raceState.equals(RaceState.WAITING))
				RaceListener.racePlayers.keySet()
						.forEach(racePlayer -> racePlayer.sendTranslatedMessage("hub.race.has_left",
								player.getTabGroupPrefix().getAsLine(racePlayer) + player.getName()));
			else if (RaceListener.raceState.equals(RaceState.RUNNING)
					|| RaceListener.raceState.equals(RaceState.LAUNCHING))
				RaceListener.racePlayers.keySet()
						.forEach(racePlayer -> racePlayer.sendTranslatedMessage("hub.race.has_abandoned",
								player.getTabGroupPrefix().getAsLine(racePlayer) + player.getName()));
			RaceListener.racePlayers.remove(player);
		}
		AuthUtils.tempPlayersKeys.remove(player.getName().toLowerCase());
	}

}

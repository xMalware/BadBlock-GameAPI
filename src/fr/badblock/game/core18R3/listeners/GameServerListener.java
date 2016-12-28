package fr.badblock.game.core18R3.listeners;

import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.gameserver.GameServerManager;
import fr.badblock.game.core18R3.gameserver.threading.GameServerKeeperAliveTask;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.run.RunType;

public class GameServerListener extends BadListener {

	@EventHandler (priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		// On renvoit les infos mises � jour, avec 0 joueurs en plus envoy� � Docker car lors du PlayerJoinEvent le joueur est d�j� list� dans la liste des joueurs connect�s
		update(event.getPlayer(), 0, true);
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onQuit(PlayerQuitEvent event) {
		// On update, avec -1 joueurs en plus envoy� � Docker car lors du PlayerQuitEvent le joueur est toujours list� dans la liste des joueurs connect�s
		update(event.getPlayer(), -1, false);
	}

	public void update(Player player, int addedPlayers, boolean joinedMessage) {
		GamePlugin gamePlugin = GamePlugin.getInstance();
		// En mode test, pouf!
		if (GameAPI.TEST_MODE) return;
		Server server = gamePlugin.getServer();
		String serverName = server.getServerName();
		GameServerManager gameServerManager = gamePlugin.getGameServerManager();
		GameServerKeeperAliveTask gameServerKeeperAliveTask = gameServerManager.getGameServerKeeperAliveTask();
		gameServerKeeperAliveTask.incrementJoinTime();
		gameServerKeeperAliveTask.keepAlive(addedPlayers);

		if (joinedMessage) {
			GameAPI.i18n().sendMessage(player, "gameserver.join", serverName);
			if (GameAPI.getAPI().getRunType().equals(RunType.GAME)) {
				if (gamePlugin.getServerXpBonus() > 1 || gamePlugin.getServerBadcoinsBonus() > 1) {
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 100, 1);
					player.sendMessage("§8§m---------------------------------------------");
					player.sendMessage("§a§l✘ Événement actif ✘");
					player.sendMessage("§8§m---------------------------------------------");
					player.sendMessage("§a§l➤ §r§aBadCoins multiplié par §b" + gamePlugin.getServerBadcoinsBonus());
					player.sendMessage("§a§l➤ §r§aXP multiplié par §b" + gamePlugin.getServerXpBonus());
					player.sendMessage("§8§m---------------------------------------------");		
					// TODO: pas sous i18n pour le moment la flemme
				}
			}
		}
	}
}

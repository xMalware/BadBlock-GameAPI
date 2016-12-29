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
import fr.badblock.gameapi.utils.general.TimeUnit;

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
					// TODO i18n
					player.sendMessage("§6+" + ((int)(gamePlugin.getServerBadcoinsBonus() * 100)) + "% §7BadCoins §e(Event serveur)");
					player.sendMessage("§3+" + ((int)(gamePlugin.getServerXpBonus() * 100)) + "% §7XP §e(Event serveur)");
				}
				if (gamePlugin.getBooster() != null) {
					if (gamePlugin.getBooster().isEnabled() && gamePlugin.getBooster().isValid()) {
						player.sendMessage("§6+" + ((int)(gamePlugin.getBooster().getBooster().getCoinsMultiplier() * 100)) + "% §7BadCoins §e(Jeu boosté par §a§l" + gamePlugin.getBooster().getUsername() + "§e)");		
						player.sendMessage("§3+" + ((int)(gamePlugin.getBooster().getBooster().getXpMultiplier() * 100)) + "% §7XP §e(Jeu boosté par §a§l" + gamePlugin.getBooster().getUsername() + "§e)");		
						player.sendMessage("§7Fin du booster de §a§l" + gamePlugin.getBooster().getUsername() + " §7: §c" + TimeUnit.SECOND.toFrench((gamePlugin.getBooster().getExpire() / 1000L) - (System.currentTimeMillis() / 1000L)));
					}
				}
			}
		}
	}
}

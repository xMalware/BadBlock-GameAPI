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
				boolean started = false;
				boolean finished = false;
				int xpBonus = 0;
				int badcoinsBonus = 0;
				int boosts = 0;
				if (gamePlugin.getServerXpBonus() > 1 || gamePlugin.getServerBadcoinsBonus() > 1) {
					// TODO i18n
					if (!started) {
						player.sendMessage("§8§l§m---------------------------------------------");
						started = true;
					}
					player.sendMessage("§6+" + ((int)((gamePlugin.getServerBadcoinsBonus() - 1) * 100)) + "% §7BadCoins (Event serveur)");
					player.sendMessage("§3+" + ((int)((gamePlugin.getServerXpBonus() - 1) * 100)) + "% §7XP (Event serveur)");
					badcoinsBonus += ((int)((gamePlugin.getServerBadcoinsBonus() - 1) * 100));
					xpBonus += ((int)((gamePlugin.getServerXpBonus() - 1) * 100));
					boosts++;
				}
				if (gamePlugin.getBooster() != null) {
					if (gamePlugin.getBooster().isEnabled() && gamePlugin.getBooster().isValid()) {
						if (!started) {
							player.sendMessage("§8§l§m---------------------------------------------");
							started = true;
						}
						player.sendMessage("§6+" + ((int)((gamePlugin.getBooster().getBooster().getCoinsMultiplier() - 1) * 100)) + "% §7BadCoins (Jeu boosté par §a§l" + gamePlugin.getBooster().getUsername() + "§7)");		
						player.sendMessage("§3+" + ((int)((gamePlugin.getBooster().getBooster().getXpMultiplier() -1) * 100)) + "% §7XP (Jeu boosté par §a§l" + gamePlugin.getBooster().getUsername() + "§7)");		
						player.sendMessage("§7Fin du booster de §a§l" + gamePlugin.getBooster().getUsername() + " §7: §c" + TimeUnit.SECOND.toFrench((gamePlugin.getBooster().getExpire() / 1000L) - (System.currentTimeMillis() / 1000L)));
						badcoinsBonus += ((int)((gamePlugin.getBooster().getBooster().getCoinsMultiplier() - 1) * 100));
						xpBonus += ((int)((gamePlugin.getBooster().getBooster().getXpMultiplier() - 1) * 100));
						boosts++;
					}
				}
				if (started && !finished) {
					if (boosts > 1) {
						player.sendMessage("§8§l§m---------------------------------------------");
						player.sendMessage("§7Total: §6+" + badcoinsBonus + "% §7BadCoins (X" + ((badcoinsBonus / 100) + 1) + ") §8- §3+" + xpBonus + "% §7XP (X" + ((xpBonus / 100) + 1) + ")");
					}
					player.sendMessage("§8§l§m---------------------------------------------");
					finished = true;
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 100, 1);
				}
			}
		}
	}
}

package fr.badblock.game.core18R3.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.gameserver.GameServer;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.players.data.GamePlayerData;
import fr.badblock.game.core18R3.players.ingamedata.GameBadblockPlayerData;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.events.api.PlayerReconnectionPropositionEvent;
import fr.badblock.gameapi.game.GameServer.WhileRunningConnectionTypes;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.run.RunType;

public class DisconnectListener extends BadListener {
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		player.removeBossBars();
		player.undisguise();

		if (GameAPI.getAPI().getRunType().equals(RunType.GAME) && !afterGame()) {
			if(GameAPI.getAPI().isLeaverBusterEnabled() && player.getBadblockMode() != BadblockMode.SPECTATOR && !player.hasPermission("api.leaverbuster.bypass")){
				player.getPlayerData().getLeaves().add(System.currentTimeMillis());
				player.saveData();
			}
			GamePlayerData gpd = (GamePlayerData) player.getPlayerData();
			if (gpd.getAddedRankedPoints() != 0) {
				String name = GamePlugin.getInstance().getGameServerManager().getGameServerConfig().getRankedName();
				if (name != null) {
					GamePlugin.getAPI().getSqlDatabase().call("UPDATE rankeds SET " + name + "=" + name + (gpd.getAddedRankedPoints() > 0 ? "+" : "-") + Math.abs(gpd.getAddedRankedPoints()) + " WHERE playerName = '" + player.getName() + "'", SQLRequestType.UPDATE);
				}
			}
			// Booster
			PlayerBooster playerBoosterZ = null;
			for (PlayerBooster playerBoosterr : player.getPlayerData().getBoosters())
				if (playerBoosterr.isEnabled() && !playerBoosterr.isExpired()) playerBoosterZ = playerBoosterr;
			boolean hasBoosterEnabled = playerBoosterZ != null;
			if (hasBoosterEnabled) {
				List<String> players = new ArrayList<String>();
				double xp = 0;
				double badcoins = 0;
				for (Player playerz : Bukkit.getOnlinePlayers()) {
					BadblockPlayer bbPlayer = (BadblockPlayer) playerz;
					if (bbPlayer.equals(player)) continue;
					PlayerBooster playerBooster = null;
					for (PlayerBooster playerBoosterr : bbPlayer.getPlayerData().getBoosters())
						if (playerBoosterr.isEnabled() && !playerBoosterr.isExpired()) playerBooster = playerBoosterr;
					if (playerBooster != null) {
						xp += playerBooster.getBooster().getXpMultiplier();
						badcoins += playerBooster.getBooster().getCoinsMultiplier();
					}
				}
				if (xp == 0) xp = 1;
				if (badcoins == 0) badcoins = 1;
				String o = "[";
				Iterator<String> iterator = players.iterator();
				while (iterator.hasNext()) {
					String oo = iterator.next();
					o += oo + (iterator.hasNext() ? ", " : "");
				}
				o += "]";
				if (xp > 1 || badcoins > 1) {
					final double xpp = xp;
					final double badcoinss = badcoins;
					final String oo = o;
					Bukkit.getOnlinePlayers().parallelStream().filter(po -> !po.equals(player)).forEach(po -> {
						BadblockPlayer pob = (BadblockPlayer) po;
						pob.sendTranslatedMessage("booster.unload", Double.toString(xpp), Double.toString(badcoinss), player.getName(), oo);
						pob.playSound(Sound.LEVEL_UP);
					});
				}
			}
		}

		if(GameAPI.getAPI().getGameServer().getGameState() != GameState.RUNNING) {
			if(player.getTeam() != null)
				player.getTeam().leaveTeam(player);
		} else {
			GameServer server = GamePlugin.getInstance().getGameServer();

			if(server.isSaving() && server.getPlay().contains(player.getUniqueId())){
				server.getSavedPlayers().add(new GameBadblockPlayerData((GameBadblockPlayer) e.getPlayer()));
			}

			boolean backup = server.getType() == WhileRunningConnectionTypes.BACKUP;

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
				server.remember(player);
		}
	}
}
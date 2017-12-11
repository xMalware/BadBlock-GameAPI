package fr.badblock.game.core18R3.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.gson.JsonObject;

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
import fr.badblock.gameapi.game.rankeds.RankedCalc;
import fr.badblock.gameapi.game.rankeds.RankedManager;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class DisconnectListener extends BadListener {
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		player.removeBossBars();
		player.undisguise();
		LoginListener.l.add(player.getName());

		if (!player.getGameMode().equals(GameMode.SPECTATOR) && GameAPI.getAPI().getRunType().equals(RunType.GAME) && GameAPI.getAPI().getGameServer().getGameState().equals(GameState.RUNNING) && GameAPI.getAPI().isLeaverBusterEnabled() && !player.getBadblockMode().equals(BadblockMode.SPECTATOR) && !player.hasPermission("api.leaverbuster.bypass")){
			List<Long> leaves = player.getLeaves();
			leaves.add(System.currentTimeMillis());
			JsonObject jsonObject = new JsonObject();
			jsonObject.add("leaves", GameAPI.getGson().toJsonTree(leaves));
			player.sendTranslatedMessage("game.addedleaverbuster");
			GameAPI.getAPI().getLadderDatabase().updatePlayerData(player.getName(), jsonObject);
			TaskManager.runTaskLater(new Runnable() {
				@Override
				public void run() {
					GameAPI.getAPI().getLadderDatabase().updatePlayerData(player.getName(), jsonObject);
				}
			}, 5);
			TaskManager.runTaskLater(new Runnable() {
				@Override
				public void run() {
					GameAPI.getAPI().getLadderDatabase().updatePlayerData(player.getName(), jsonObject);
				}
			}, 20);
			TaskManager.runTaskLater(new Runnable() {
				@Override
				public void run() {
					GameAPI.getAPI().getLadderDatabase().updatePlayerData(player.getName(), jsonObject);
				}
			}, 20*5);
		}
		if (GameAPI.getAPI().getRunType().equals(RunType.GAME) && !afterGame()) {
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

		if (afterGame()) {
			GamePlayerData gpd = (GamePlayerData) player.getPlayerData();
			if (gpd.getAddedRankedPoints() != 0 && GamePlugin.getInstance().getGameServerManager().getRankedConfig().ranked) {
				String name = GamePlugin.getInstance().getGameServerManager().getRankedConfig().getRankedName();
				if (name != null) {
					GamePlugin.getAPI().getSqlDatabase().call("SELECT " + name + " FROM rankeds WHERE playerName = '" + player.getName() + "'", SQLRequestType.QUERY, new Callback<ResultSet>() {

						@Override
						public void done(ResultSet result, Throwable error) {
							try {
								result.next();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							long count;
							try {
								count = result.getInt(name);
								if (gpd.getAddedRankedPoints() < 0)
									count -= Math.abs(gpd.getAddedRankedPoints());
								else count += Math.abs(gpd.getAddedRankedPoints());
								if (count < 0) {
									count = 0;
								}
								GamePlugin.getAPI().getSqlDatabase().call("UPDATE rankeds SET " + name + "=" + count + " WHERE playerName = '" + player.getName() + "'", SQLRequestType.UPDATE);
							} catch (SQLException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}

		if(GameAPI.getAPI().getGameServer().getGameState() != GameState.RUNNING) {
			if(player.getTeam() != null)
				player.getTeam().leaveTeam(player);
		} else {
			// TODO something else
			if (GamePlugin.getAPI().getRunType().equals(RunType.GAME))
			{
				player.getPlayerData().incrementTempRankedData(RankedManager.instance.getCurrentRankedGameName(), "looses", 1);
				player.getPlayerData().incrementTempRankedData(RankedManager.instance.getCurrentRankedGameName(), "deaths", 1);
				RankedManager.instance.calcPoints(RankedManager.instance.getCurrentRankedGameName(), player, new RankedCalc()
				{

					@Override
					public long done() {
						return -2;
					}

				});
				RankedManager.instance.fill(RankedManager.instance.getCurrentRankedGameName(), player.getName());
			}

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
package fr.badblock.game.core18R3.players.utils;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.AbstractMap.SimpleEntry;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import com.google.gson.JsonObject;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.players.listeners.GameScoreboard;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.events.PartyJoinEvent;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.game.rankeds.RankedManager;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.RankedPlayer;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.general.Callback;
import net.md_5.bungee.api.ChatColor;

public class PlayerLoginWorkers
{

	public static void workAsync(GameBadblockPlayer player)
	{
		new Thread("loadPlayer-" + player.getName())
		{
			@Override
			public void run()
			{
				loadNick(player);
				loadRankeds(player);
				loadPlayerData(player);
				loadCustomRank(player);
			}
		}.start();
	}

	public static void loadNick(GameBadblockPlayer player)
	{
		try
		{
			Statement statement = GameAPI.getAPI().getSqlDatabase().createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT playerName FROM nick WHERE nick = '" + player.getName() + "'");
			String realName = null;
			if (resultSet.next())
			{
				String playerName = resultSet.getString("playerName");
				if (!playerName.isEmpty())
				{
					realName = playerName;
				}
			}
			player.setRealName(realName);
			resultSet.close();
			statement.close();
		}
		catch (Exception error)
		{
			error.printStackTrace();
		}
	}

	public static void loadRankeds(GameBadblockPlayer player)
	{
		if (!GameAPI.getAPI().getRunType().equals(RunType.GAME))
		{
			return;
		}
		RankedManager rm = RankedManager.instance;
		String game = rm.getCurrentRankedGameName();
		rm.getMonthRank(game, player, new Callback<Integer>()
		{

			@Override
			public void done(Integer result, Throwable error) {
				player.setMonthRank(result.intValue());
			}

		});
		rm.getTotalRank(game, player, new Callback<Integer>()
		{

			@Override
			public void done(Integer result, Throwable error) {
				player.setTotalRank(result.intValue());
			}

		});
		rm.getTotalPoints(game, player, new Callback<Integer>()
		{

			@Override
			public void done(Integer result, Throwable error) {
				player.setTotalPoints(result.intValue());
			}

		});
		player.setRanked(new RankedPlayer(player, player.getTotalPoints(), player.getTotalRank(), player.getMonthRank()));
	}

	public static void loadCustomRank(GameBadblockPlayer player)
	{
		if (player.getPermissions() != null && player.getPermissions().getParent() != null)
		{
			if (player.getPermissions().getParent().getName().equalsIgnoreCase("gradeperso") || player.getPermissions().getAlternateGroups().containsKey("gradeperso"))
			{
				GamePlugin.getInstance().getWebDatabase().call("SELECT gradeperso, customcolor FROM joueurs WHERE pseudo = '" + 
						GamePlugin.getInstance().getWebDatabase().mysql_real_escape_string(player.getName()) + "'", SQLRequestType.QUERY, new Callback<ResultSet>()
				{

					@Override
					public void done(ResultSet result, Throwable error)
					{
						try
						{
							if (result.next())
							{
								player.setCustomRank(ChatColor.translateAlternateColorCodes('&', result.getString("gradeperso")) + " ");
								player.setCustomColor(ChatColor.translateAlternateColorCodes('&', result.getString("customcolor")));

								// find group
								String rank = GameScoreboard.customRankId;
								String id = GameScoreboard.gsb.generateForId(GameScoreboard.customRanks.size()) + "";
								rank += id;
								GameScoreboard.customRanks.put(rank, new SimpleEntry<String, String>(player.getCustomRank(), player.getName()));
								GameScoreboard.groups.put(rank, rank);

								if (GameScoreboard.board.getTeam(rank) == null){
									GameScoreboard.board.registerNewTeam(rank);
								}

								Team teamHandler = GameScoreboard.board.getTeam(rank);

								teamHandler.setAllowFriendlyFire(true);
								for (BadblockPlayer plo : BukkitUtils.getAllPlayers())
								{
									GameScoreboard.gsb.sendTeamData(rank, player.getCustomRank(), plo);
								}
								GameScoreboard.gsb.sendTeamData(rank, player.getCustomRank(), player);
								if (rank != null)
								{
									Team team = GameScoreboard.board.getEntryTeam(player.getName());
									if (team != null && !team.getName().equals(rank))
									{
										team.removeEntry(player.getName());
									}
									final String ranke = rank;
									new BukkitRunnable() {
										@Override
										public void run() {
											GameScoreboard.gsb.sendTeamData(ranke, player.getCustomRank(), player);
											GameScoreboard.board.getTeam(ranke).addEntry(player.getName());
										}
									}.runTaskLater(GameAPI.getAPI(), 5L);
								}
							}
							result.close();
						}
						catch (Exception error34)
						{
							error34.printStackTrace();
						}
					}
				});
			}
		}
	}

	public static void loadPlayerData(GameBadblockPlayer player)
	{
		GameAPI.getAPI().getLadderDatabase().getPlayerData(player.getRealName() != null ? player.getRealName() : player.getName(), new Callback<JsonObject>() {
			@Override
			public void done(JsonObject result, Throwable error) {
				new Thread() {
					@Override
					public void run() {
						player.setObject(result);
						player.updateData(result);

						while (!player.isHasJoined())
							try {
								Thread.sleep(10L);
							} catch (InterruptedException unused) {}

						player.setDataFetch(true);
						synchronized (Bukkit.getServer()) {
							if (player.getPlayersWithHim() != null && !player.getPlayersWithHim().isEmpty())
								Bukkit.getPluginManager().callEvent(new PartyJoinEvent(player, player.getPlayersWithHim()));
							Bukkit.getPluginManager().callEvent(new PlayerLoadedEvent(player));
						}
					}
				}.start();
			}
		});
	}

}

package fr.badblock.game.core18R3.gameserver;

import java.sql.ResultSet;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.game.rankeds.RankedCalc;
import fr.badblock.gameapi.game.rankeds.RankedManager;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;

public class RealRankedManager extends RankedManager {

	public Map<String, List<String>> gameFields = new HashMap<>();
	//			Nom jeu		Pseudo		// field // value
	public Map<String, Map<String, Map<String, Long>>> temp = new HashMap<>();

	@Override
	public String getCurrentRankedGameName()
	{
		return GamePlugin.getInstance().getGameServerManager().getRankedConfig().rankedName;
	}

	@Override
	public void initialize(String gameName, String... fields) {
		if (!GamePlugin.getInstance().getGameServerManager().getRankedConfig().ranked)
		{
			System.out.println("No rankeds. Cancelled initializing.");
		}
		// Par mois
		List<String> fieldList = new ArrayList<>();
		fieldList.add("_points");
		fieldList.addAll(Arrays.asList(fields));
		gameFields.put(gameName, fieldList);
		String message = "CREATE TABLE IF NOT EXISTS " + getTempTableName(gameName) + " (" + 
				"	`id` INT(255) NOT NULL AUTO_INCREMENT, " + 
				"	PRIMARY KEY (`id`)," + 
				"	`playerName` VARCHAR(255) NULL";
		for (String field : fieldList)
		{
			message += ",	`" + field + "` BIGINT(255) NOT NULL DEFAULT '0'";
		}
		message +=
				") " + 
						"COLLATE='utf8_general_ci' " + 
						"ENGINE=InnoDB;";
		System.out.println("[SQL Request] " + message);
		GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
		// Total
		message = "CREATE TABLE IF NOT EXISTS " + getPermanentTableName(gameName) + " (" + 
				"	`id` INT(255) NOT NULL AUTO_INCREMENT, " + 
				"	PRIMARY KEY (`id`)," + 
				"	`playerName` VARCHAR(255) NULL";
		for (String field : fieldList)
		{
			message += ",	`" + field + "` BIGINT(255) NOT NULL DEFAULT '0'";
		}
		message +=
				") " + 
						"COLLATE='utf8_general_ci' " + 
						"ENGINE=InnoDB;";
		System.out.println("[SQL Request] " + message);
		GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
	}

	@Override
	public void calcPoints(String gameName, BadblockPlayer player, RankedCalc calc)
	{
		long points = calc.done();
		player.getPlayerData().setTempRankedData(gameName, "_points", points);
	}

	@Override
	public long getData(String gameName, BadblockPlayer player, String fieldName) {
		GameBadblockPlayer gameBadBlockPlayer = (GameBadblockPlayer) player;
		String playerName = gameBadBlockPlayer.getRealName() != null ? gameBadBlockPlayer.getRealName() : gameBadBlockPlayer.getName();
		RealRankedManager realRankedManager = (RealRankedManager) RankedManager.instance;
		Map<String, Map<String, Long>> gameValues = realRankedManager.temp.get(gameName);
		if (gameValues == null) gameValues = new HashMap<>();
		Map<String, Long> playerValues = gameValues.get(playerName);
		if (playerValues == null) playerValues = new HashMap<>();
		long data = playerValues.containsKey(fieldName) ? playerValues.get(fieldName) : 0;
		return data;
	}

	@Override
	public void fill(String gameName, String name) {
		Map<String, Map<String, Long>> gameValues = temp.get(gameName);
		Set<Entry<String, Long>> entry = gameValues.get(name).entrySet();
		String[] tables = new String[] { getTempTableName(gameName), getPermanentTableName(gameName) };
		for (String table : tables)
		{
			System.out.println("[SQL Request] SELECT COUNT(id) AS count FROM " + table + " WHERE playerName = '" + name + "'");
			GameAPI.getAPI().getSqlDatabase().call("SELECT COUNT(id) AS count FROM " + table + " WHERE playerName = '" + name + "'", SQLRequestType.QUERY, new Callback<ResultSet>()
			{

				@Override
				public void done(ResultSet result, Throwable error) {
					System.out.println("[SQL Request] A");
					try
					{
						if (result.next() && result.getInt("count") > 0)
						{
							System.out.println("[SQL Request] B");
							String valuesBuilder = "";
							Iterator<Entry<String, Long>> iterator = entry.iterator();
							boolean points = false;
							while (iterator.hasNext())
							{
								Entry<String, Long> currentEntry = iterator.next();
								if (currentEntry.getKey().equals("_points"))
								{
									points = true;
								}
								valuesBuilder += currentEntry.getKey() + "=" + currentEntry.getKey() + "+" + currentEntry.getValue();
								if (iterator.hasNext())
								{
									valuesBuilder += ", ";
								}
							}
							if (!points) return;
							String message = "UPDATE " + table + " SET " + valuesBuilder + " WHERE playerName = '" + name + "'";
							System.out.println("[SQL Request] " + message);
							GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
						}
						else
						{
							String fieldsBuilder = "";
							Iterator<Entry<String, Long>> iterator = entry.iterator();
							boolean points = false;
							while (iterator.hasNext())
							{
								Entry<String, Long> o = iterator.next();
								if (o.getKey().equals("_points"))
								{
									points = true;
								}
								fieldsBuilder += ", " + o.getKey();
							}
							String valuesBuilder = "";
							for (Entry<String, Long> part : entry)
							{
								valuesBuilder += ", '" + part.getValue() + "'";
							}
							if (!points) return;
							String message = "INSERT INTO " + table + "(playerName" + fieldsBuilder + ") VALUES('" + name + "'" + valuesBuilder + ")";
							System.out.println("[SQL Request] " + message);
							GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
						}
						result.close();
					}
					catch(Exception exception)
					{
						System.out.println("[SQL Request] C");
						exception.printStackTrace();
					}
				}

			});
		}
	}
	
	@Override
	public void fill(String gameName) {
		if (!gameFields.containsKey(gameName))
		{
			new RuntimeException("Unknown fields for " + gameName);
		}
		if (!temp.containsKey(gameName))
		{
			System.out.println("[INFO] Unable to fill ranking data. No data.");
		}
		Map<String, Map<String, Long>> gameValues = temp.get(gameName);
		gameValues.entrySet().forEach(entry -> fill(gameName, entry.getKey()));
	}

	private String getTempTableName(String gameName)
	{
		Date date = new Date();
		@SuppressWarnings("deprecation")
		String month = DateFormatSymbols.getInstance(Locale.FRENCH).getMonths()[date.getMonth()];
		SimpleDateFormat ffr = new SimpleDateFormat("yyyy", new Locale("fr"));
		String year = ffr.format(date);
		return "rankeds." + gameName + "_" + month + "_" + year;
	}

	private String getPermanentTableName(String gameName)
	{
		return "rankeds." + gameName + "_all";
	}

	@Override
	public void getTotalRank(String gameName, BadblockPlayer player, Callback<Integer> callback) {
		GameAPI.getAPI().getSqlDatabase().call("SELECT FIND_IN_SET( _points, (    " + 
				"SELECT GROUP_CONCAT( _points" + 
				"ORDER BY _points DESC ) " + 
				"FROM " + getPermanentTableName(gameName)  + " )" + 
				") AS rank\r\n" + 
				"FROM " + getPermanentTableName(gameName) +
				" WHERE playerName =  '" + player.getName() + "'", SQLRequestType.QUERY, new Callback<ResultSet>()
		{

			@Override
			public void done(ResultSet result, Throwable error) {
				try
				{
					int rank = -1;
					if (result.next() && result.getInt("rank") > 0)
					{
						rank = result.getInt("rank");
					}
					callback.done(rank, null);
					result.close();
				}
				catch(Exception exception)
				{
					exception.printStackTrace();
				}
			}

		});
	}

	@Override
	public void getTotalPoints(String gameName, BadblockPlayer player, Callback<Integer> callback) {
		GameAPI.getAPI().getSqlDatabase().call("SELECT _points FROM " + getPermanentTableName(gameName)  + " FROM " + getPermanentTableName(gameName) + " WHERE playerName =  '" + player.getName() + "'", SQLRequestType.QUERY, new Callback<ResultSet>()
		{

			@Override
			public void done(ResultSet result, Throwable error) {
				try
				{
					int points = 0;
					if (result.next())
					{
						points = result.getInt("_points");
					}
					callback.done(points, null);
					result.close();
				}
				catch(Exception exception)
				{
					exception.printStackTrace();
				}
			}

		});
	}

	@Override
	public void getMonthRank(String gameName, BadblockPlayer player, Callback<Integer> callback) {
		GameAPI.getAPI().getSqlDatabase().call("SELECT FIND_IN_SET( _points, (    " + 
				"SELECT GROUP_CONCAT( _points" + 
				"ORDER BY _points DESC ) " + 
				"FROM " + getTempTableName(gameName)  + " )" + 
				") AS rank\r\n" + 
				"FROM " + getTempTableName(gameName) +
				" WHERE playerName =  '" + player.getName() + "'", SQLRequestType.QUERY, new Callback<ResultSet>()
		{

			@Override
			public void done(ResultSet result, Throwable error) {
				try
				{
					int rank = -1;
					if (result.next() && result.getInt("rank") > 0)
					{
						rank = result.getInt("rank");
					}
					callback.done(rank, null);
					result.close();
				}
				catch(Exception exception)
				{
					exception.printStackTrace();
				}
			}

		});
	}

}

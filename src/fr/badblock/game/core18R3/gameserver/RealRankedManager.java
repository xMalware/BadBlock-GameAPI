package fr.badblock.game.core18R3.gameserver;

import java.sql.ResultSet;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.game.rankeds.RankedManager;
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
		List<String> fieldList = Arrays.asList(fields);
		gameFields.put(gameName, fieldList);
		String message = "CREATE TABLE IF NOT EXISTS `" + getTempTableName(gameName) + "` (" + 
				"	`id` INT(255) NOT NULL AUTO_INCREMENT, " + 
				"	PRIMARY KEY (`id`) " + 
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
		message = "CREATE TABLE IF NOT EXISTS `" + getPermanentTableName(gameName) + "` (" + 
				"	`id` INT(255) NOT NULL AUTO_INCREMENT, " + 
				"	PRIMARY KEY (`id`) " + 
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
		gameValues.entrySet().forEach(entry ->
		{
			String name = entry.getKey();
			String[] tables = new String[] { getTempTableName(gameName), getPermanentTableName(gameName) };
			for (String table : tables)
			{
				GameAPI.getAPI().getSqlDatabase().call("SELECT COUNT(id) AS count FROM " + table + " WHERE playerName = '" + name + "'", SQLRequestType.QUERY, new Callback<ResultSet>()
				{

					@Override
					public void done(ResultSet result, Throwable error) {
						System.out.println("[SQL Request] A");
						try
						{
							if (result.next())
							{
								System.out.println("[SQL Request] B");
								String valuesBuilder = "";
								Iterator<Entry<String, Long>> iterator = entry.getValue().entrySet().iterator();
								while (iterator.hasNext())
								{
									Entry<String, Long> currentEntry = iterator.next();
									valuesBuilder += currentEntry.getKey() + "=" + currentEntry.getKey() + "+" + currentEntry.getValue();
									if (iterator.hasNext())
									{
										valuesBuilder += ", ";
									}
								}
								String message = "UPDATE " + table + " SET " + valuesBuilder + " WHERE playerName = '" + name + "'";
								GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
							}
							else
							{
								String fieldsBuilder = "";
								Iterator<String> iterator = entry.getValue().keySet().iterator();
								while (iterator.hasNext())
								{
									fieldsBuilder += ", " + iterator.next();
								}
								String valuesBuilder = "";
								for (long part : entry.getValue().values())
								{
									valuesBuilder += ", '" + part + "'";
								}
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
		});
	}

	private String getTempTableName(String gameName)
	{
		Date date = new Date();
		@SuppressWarnings("deprecation")
		String month = DateFormatSymbols.getInstance(Locale.FRENCH).getMonths()[date.getMonth()];
		SimpleDateFormat ffr = new SimpleDateFormat("yyyy", new Locale("fr"));
		String year = ffr.format(date);
		return "rankeds." + gameName + "_" + month + "-" + year;
	}

	private String getPermanentTableName(String gameName)
	{
		return "rankeds." + gameName + "_all";
	}

}

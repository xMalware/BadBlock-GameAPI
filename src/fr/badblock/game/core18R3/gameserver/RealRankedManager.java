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

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.game.rankeds.RankedManager;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;

public class RealRankedManager extends RankedManager {

	public Map<String, List<String>> gameFields = new HashMap<>();
	//			Nom jeu		Pseudo		// field // value
	public Map<String, Map<String, Map<String, Long>>> temp = new HashMap<>();

	@Override
	public void initialize(String gameName, String... fields) {
		// Par mois
		List<String> fieldList = Arrays.asList(fields);
		gameFields.put(gameName, fieldList);
		String message = "CREATE TABLE IF NOT EXISTS `" + getTempTableName(gameName) + "` (" + 
				"	`id` INT NULL," + 
				"	`playerName` VARCHAR(255) NULL,";
		for (String field : fieldList)
		{
			message += "	`" + field + "` BIGINT(255) NOT NULL DEFAULT '0',";
		}
		message +=
				")" + 
						"COLLATE='utf8_general_ci'" + 
						"ENGINE=InnoDB;";
		System.out.println("[SQL Request] " + message);
		GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
		// Total
		message = "CREATE TABLE IF NOT EXISTS `" + getPermanentTableName(gameName) + "` (" + 
				"	`id` INT NULL," + 
				"	`playerName` VARCHAR(255) NULL,";
		for (String field : fieldList)
		{
			message += "	`" + field + "` BIGINT(255) NOT NULL DEFAULT '0',";
		}
		message +=
				")" + 
						"COLLATE='utf8_general_ci'" + 
						"ENGINE=InnoDB;";
		System.out.println("[SQL Request] " + message);
		GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
	}

	@Override
	public void fill(String gameName, BadblockPlayer badblockPlayer, long... data) {
		if (!gameFields.containsKey(gameName))
		{
			new RuntimeException("Unknown fields for " + gameName);
		}
		List<String> fields = gameFields.get(gameName);
		if (fields.size() != data.length)
		{
			new RuntimeException("Conflict with field length (" + fields.size() + " vs " + data.length + ") for " + gameName);
		}
		GameBadblockPlayer gameBadBlockPlayer = (GameBadblockPlayer) badblockPlayer;
		String name = gameBadBlockPlayer.getRealName() != null ? gameBadBlockPlayer.getRealName() : null;
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
							int i = 0;
							Iterator<String> iterator = fields.iterator();
							while (iterator.hasNext())
							{
								String field = iterator.next();
								long value = data[i];
								valuesBuilder += field + "=" + field + "+" + value;
								if (iterator.hasNext())
								{
									valuesBuilder += ", ";
								}
								i++;
							}
							String message = "UPDATE " + table + " SET " + valuesBuilder + " WHERE playerName = '" + name + "'";
							GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
						}
						else
						{
							String fieldsBuilder = "";
							Iterator<String> iterator = fields.iterator();
							while (iterator.hasNext())
							{
								fieldsBuilder += ", " + iterator.next();
							}
							String valuesBuilder = "";
							for (long part : data)
							{
								valuesBuilder += ", '" + part + "'";
							}
							String message = "INSERT INTO " + table + "(playerName" + fieldsBuilder + ") VALUES('" + badblockPlayer.getName() + "'" + valuesBuilder + ")";
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

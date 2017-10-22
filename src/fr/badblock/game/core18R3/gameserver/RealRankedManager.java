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
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.general.Callback;

public class RealRankedManager extends RankedManager {

	private Map<BadblockGame, List<String>> gameFields = new HashMap<>();

	@Override
	public void initialize(BadblockGame badblockGame, String... fields) {
		// Par mois
		List<String> fieldList = Arrays.asList(fields);
		gameFields.put(badblockGame, fieldList);
		String message = "CREATE TABLE IF NOT EXISTS `" + getTempTableName(badblockGame) + "` (" + 
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
		GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
		// Total
		message = "CREATE TABLE IF NOT EXISTS `" + getPermanentTableName(badblockGame) + "` (" + 
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
		GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
	}

	@Override
	public void fill(BadblockGame badblockGame, BadblockPlayer badblockPlayer, long... data) {
		if (!gameFields.containsKey(badblockGame))
		{
			new RuntimeException("Unknown fields for " + badblockGame.getInternalGameName());
		}
		List<String> fields = gameFields.get(badblockGame);
		if (fields.size() != data.length)
		{
			new RuntimeException("Conflict with field length (" + fields.size() + " vs " + data.length + ") for " + badblockGame.getInternalGameName());
		}
		GameBadblockPlayer gameBadBlockPlayer = (GameBadblockPlayer) badblockPlayer;
		String name = gameBadBlockPlayer.getRealName() != null ? gameBadBlockPlayer.getRealName() : null;
		String[] tables = new String[] { getTempTableName(badblockGame), getPermanentTableName(badblockGame )};
		for (String table : tables)
		{
			GameAPI.getAPI().getSqlDatabase().call("SELECT COUNT(id) AS count FROM " + table + " WHERE playerName = '" + name + "'", SQLRequestType.QUERY, new Callback<ResultSet>()
			{

				@Override
				public void done(ResultSet result, Throwable error) {
					try
					{
						if (result.next())
						{
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
							GameAPI.getAPI().getSqlDatabase().call(message, SQLRequestType.UPDATE);
						}
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

	private String getTempTableName(BadblockGame badblockGame)
	{
		Date date = new Date();
		@SuppressWarnings("deprecation")
		String month = DateFormatSymbols.getInstance(Locale.FRENCH).getMonths()[date.getMonth()];
		SimpleDateFormat ffr = new SimpleDateFormat("yyyy", new Locale("fr"));
		String year = ffr.format(date);
		return "rankeds." + badblockGame.getInternalGameName() + "_" + month + "-" + year;
	}

	private String getPermanentTableName(BadblockGame badblockGame)
	{
		return "rankeds." + badblockGame.getInternalGameName() + "_all";
	}

}

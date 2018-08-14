package fr.badblock.game.core18R3.commands;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import fr.badblock.api.common.utils.i18n.ChatColor;
import fr.badblock.game.core18R3.players.listeners.GameScoreboard;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class VoteMapCommand extends AbstractCommand {

	private static List<String> names = new ArrayList<>();

	public VoteMapCommand() {
		super("votemap", new TranslatableString("commands.votemap.usage"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER);
	}

	public String getMapId()
	{
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', GameAPI.getServerName().split("_")[0] + "_" + GameScoreboard.map));
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {

		if(args.length == 0){
			return false;
		}

		if (GameScoreboard.map == null || GameScoreboard.map.isEmpty())
		{
			return true;
		}

		String rawInt = args[0];

		int stars = 0;

		try
		{
			stars = Integer.parseInt(rawInt);
		}
		catch (Exception error)
		{
			return true;
		}

		if (stars < 0 || stars > 5)
		{
			return true;
		}
		
		final int st = stars;

		String playerName = sender.getName().toLowerCase();
		if (names.contains(playerName))
		{
			GameAPI.i18n().sendMessage(sender, "commands.votemap.alreadyvoted");
			return true;
		}

		GameAPI.getAPI().getSqlDatabase().call("SELECT * FROM mapVotes WHERE playerName = '" + sender.getName() +
				"' && mapId = '" + getMapId() + "'", SQLRequestType.QUERY, new Callback<ResultSet>()
		{

			@Override
			public void done(ResultSet result, Throwable error)
			{
				try
				{
					if (result.next())
					{
						GameAPI.i18n().sendMessage(sender, "commands.votemap.alreadyvoted");
						return;
					}
					
					GameAPI.getAPI().getSqlDatabase().call("INSERT INTO mapVotes(playerName, mapId, stars) VALUES('" + sender.getName() + "', '" + getMapId()
					+ "', '" + st + "')", SQLRequestType.UPDATE);
					GameAPI.i18n().sendMessage(sender, "commands.votemap.added", st);
				}
				catch (Exception err)
				{
					err.printStackTrace();
				}
			}

		});

		return true;
	}
}
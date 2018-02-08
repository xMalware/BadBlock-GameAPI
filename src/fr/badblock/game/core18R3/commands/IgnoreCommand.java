package fr.badblock.game.core18R3.commands;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.players.data.PlayerData;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class IgnoreCommand extends AbstractCommand
{

	private final int defaultLimit = 5;

	public IgnoreCommand()
	{
		super("ignore", new TranslatableString("commands.ignore.usage"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0) return false;

		BadblockPlayer concerned = (BadblockPlayer) sender;
		PlayerData playerData = concerned.getPlayerData();

		if (playerData.getIgnoreList() == null) 
		{
			playerData.setIgnoreList(new ArrayList<>());
			concerned.saveGameData();
		}

		String type = args[0];

		switch (type)
		{
		case "list":
			Collections.sort(playerData.getIgnoreList(), Collator.getInstance());
			String list = StringUtils.join(playerData.getIgnoreList(), ", ");
			concerned.sendTranslatedMessage("commands.ignore.list", list);
			break;
		case "ajouter":
		case "insert":
		case "put":
		case "add":
			if (args.length != 2)
			{
				concerned.sendTranslatedMessage("commands.ignore.usage");
				return true;
			}
			String playerName = args[1];
			boolean containsSearchStr = playerData.getIgnoreList().stream().filter(s -> s.equalsIgnoreCase(playerName)).findFirst()
					.isPresent();
			if (containsSearchStr)
			{
				concerned.sendTranslatedMessage("commands.ignore.alreadyinlist", playerName);
			}
			else
			{
				if (!isIgnorable(playerName))
				{
					concerned.sendTranslatedMessage("commands.ignore.notignorable", playerName);
					return true;
				}
				Integer ignoreLimit = concerned.getPermissionValue("ignoreLimit", Integer.class);
				if (ignoreLimit == null)
				{
					ignoreLimit = defaultLimit;
				}
				if (playerData.getIgnoreList().size() >= ignoreLimit)
				{
					concerned.sendTranslatedMessage("commands.ignore.youreachlimit", ignoreLimit);
					return true;
				}
				playerData.getIgnoreList().add(playerName);
				concerned.saveGameData();
				concerned.sendTranslatedMessage("commands.ignore.added", playerName);
			}
			break;
		case "rm":
		case "del":
		case "erase":
		case "delete":
		case "remove":
			if (args.length != 2)
			{
				concerned.sendTranslatedMessage("commands.ignore.usage");
				return true;
			}
			playerName = args[1];
			containsSearchStr = playerData.getIgnoreList().stream().filter(s -> s.equalsIgnoreCase(playerName)).findFirst()
					.isPresent();
			if (!containsSearchStr)
			{
				concerned.sendTranslatedMessage("commands.ignore.notinlist", playerName);
			}
			else
			{
				playerData.getIgnoreList().remove(playerName);
				concerned.saveGameData();
				concerned.sendTranslatedMessage("commands.ignore.removed", playerName);
			}
			break;
		default:
			concerned.sendTranslatedMessage("commands.ignore.usage");
		}
		return true;
	}

	/**
	 * Pas très très flexi mais c'est toujours bien <3
	 * @return
	 */
	public boolean isIgnorable(String string)
	{
		return !string.equalsIgnoreCase("xmalware") && !string.equalsIgnoreCase("sulfique") &&
				!string.equalsIgnoreCase("sizas") && !string.equalsIgnoreCase("lelann")
				&& !string.equalsIgnoreCase("micro_maniaque") && !string.equalsIgnoreCase("latitchips");
	}

}
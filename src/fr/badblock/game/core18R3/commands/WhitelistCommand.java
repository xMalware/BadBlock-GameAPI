package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class WhitelistCommand extends AbstractCommand {
	public WhitelistCommand() {
		super("whitelist", new TranslatableString("commands.whitelist.usage"), GamePermission.BMODERATOR);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0) {
			return false;
		}

		switch (args[0]) {
		case "on":
			GameAPI.getAPI().setWhitelistStatus(true);
			new TranslatableString("commands.whitelist.on").send(sender);
			break;
		case "off":
			GameAPI.getAPI().setWhitelistStatus(false);
			new TranslatableString("commands.whitelist.off").send(sender);
			break;
		case "add":
			if (args.length == 1)
				return false;

			GameAPI.getAPI().whitelistPlayer(args[1]);
			new TranslatableString("commands.whitelist.add", args[0]).send(sender);
			break;
		case "remove":
			if (args.length == 1)
				return false;

			GameAPI.getAPI().unwhitelistPlayer(args[1]);
			new TranslatableString("commands.whitelist.remove", args[1]).send(sender);
			break;
		case "list":
			new TranslatableString("commands.whitelist.list",
					StringUtils.join(GameAPI.getAPI().getWhitelistedPlayers(), "&7, &b")).send(sender);
			break;
		default:
			return false;
		}

		return true;
	}
}
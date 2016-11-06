package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class AdminCommand extends AbstractCommand {
	
	public AdminCommand() {
		super("admin", new TranslatableString("commands.admin.usage"), GamePermission.ADMIN, GamePermission.ADMIN, GamePermission.ADMIN, "adm");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0) return true;
		GameAPI.i18n().sendMessage(sender, "chat.admin", sender.getName(), StringUtils.join(args, " ", 0));
		return true;
	}
	
}

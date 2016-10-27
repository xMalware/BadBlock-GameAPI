package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class SudoCommand extends AbstractCommand {
	public SudoCommand() {
		super("sudo", new TranslatableString("commands.sudo.usage"), GamePermission.ADMIN, GamePermission.ADMIN, GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length < 2)
			return false;
		
		String playerName = args[0];
		Player to = Bukkit.getPlayer(playerName);

		if (to == null) {
			sendTranslatedMessage(sender, "commands.unknowplayer", playerName);
			return true;
		}

		String message = StringUtils.join(args, " ", 1);

		if (message.startsWith("c:")) {
			to.chat(message.replace("c:", ""));
		} else {
			to.performCommand(message);
		}
		
		return true;
	}
}
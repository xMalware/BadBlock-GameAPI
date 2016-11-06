package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class AdminModeCommand extends AbstractCommand {
	public AdminModeCommand() {
		super("adminmode", new TranslatableString("commands.adminmode.usage"), GamePermission.BMODERATOR, GamePermission.ADMIN, GamePermission.ADMIN, "adminm", "am");
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = (BadblockPlayer) sender;

		concerned.setAdminMode(!concerned.hasAdminMode());
		new TranslatableString("commands.adminmode.status-" + concerned.hasAdminMode()).send(sender);

		return true;
	}
}
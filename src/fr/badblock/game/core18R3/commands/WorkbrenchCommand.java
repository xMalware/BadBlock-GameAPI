package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class WorkbrenchCommand extends AbstractCommand {
	public WorkbrenchCommand() {
		super("workbrench", new TranslatableString("commands.workbrench.usage"), GamePermission.ADMIN, "wb", "craft");
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = (BadblockPlayer) sender;
		concerned.openWorkbench(null, true);
		
		return true;
	}
}
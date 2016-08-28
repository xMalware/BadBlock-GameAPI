package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.players.ingamedata.CommandInGameData;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class InvseeCommand extends AbstractCommand {
	public InvseeCommand() {
		super("invsee", new TranslatableString("commands.invsee.usage"), GamePermission.BMODERATOR);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0)
			return false;

		BadblockPlayer concerned = (BadblockPlayer) sender;
		BadblockPlayer invsee = (BadblockPlayer) Bukkit.getPlayer(args[0]);

		if (invsee == null) {
			new TranslatableString("commands.unknowplayer", args[0]).send(sender);
			return true;
		}

		concerned.closeInventory();
		concerned.inGameData(CommandInGameData.class).invsee = true;
		concerned.openInventory(invsee.getInventory());
		return true;
	}
}
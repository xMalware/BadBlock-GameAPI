package fr.badblock.game.core18R3.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class SuicideCommand extends AbstractCommand {
	public SuicideCommand() {
		super("suicide", new TranslatableString("commands.suicide.usage"), GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = (BadblockPlayer) sender;
		concerned.damage(concerned.getHealth());
		return true;
	}
}
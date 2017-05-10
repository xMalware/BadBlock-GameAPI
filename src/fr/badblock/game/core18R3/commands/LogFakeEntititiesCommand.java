package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.fakeentities.FakeEntities;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class LogFakeEntititiesCommand extends AbstractCommand {

	public LogFakeEntititiesCommand(String command, TranslatableString usage, GamePermission perm, String[] aliases) {
		super("logfakeent", new TranslatableString(""), GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		FakeEntities.logFakeEntities();
		return false;
	}

}

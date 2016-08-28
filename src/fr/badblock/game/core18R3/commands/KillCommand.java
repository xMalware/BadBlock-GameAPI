package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class KillCommand extends AbstractCommand {
	public KillCommand() {
		super("kill", new TranslatableString("commands.kill.usage"), GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = null;

		if (args.length == 0) {
			return false;
		} else if (args.length > 0) {
			concerned = (BadblockPlayer) Bukkit.getPlayer(args[0]);
		}

		if (concerned == null) {
			new TranslatableString("commands.unknowplayer", args[0]).send(sender);
		} else {
			concerned.damage(Double.MAX_VALUE);
			concerned.sendTranslatedMessage("commands.kill.killed");

			if (!concerned.equals(sender)) {
				sendTranslatedMessage(sender, "commands.kill.killedplayer", concerned.getName());
			}
		}

		return true;
	}
}
package fr.badblock.game.core18R3.commands;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class SudoCommand extends AbstractCommand {
	public SudoCommand() {
		super("sudo", new TranslatableString("commands.sudo.usage"), GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length != 2) return false;
		BadblockPlayer concerned = (BadblockPlayer) sender;
		String playerName = args[0];
		Player to = Bukkit.getPlayer(playerName);
		if (to == null) {
			concerned.sendTranslatedMessage("commands.sudo.offline", playerName);
			return true;
		}
		BadblockPlayer player = (BadblockPlayer) to;
		String message = args[1];
		if (message.startsWith("c:")) {
			to.chat(message.replace("c:", ""));
		}else{
			to.performCommand(message);
		}
		return true;
	}
}
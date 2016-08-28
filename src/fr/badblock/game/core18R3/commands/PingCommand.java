package fr.badblock.game.core18R3.commands;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class PingCommand extends AbstractCommand {
	public PingCommand() {
		super("ping", new TranslatableString("commands.ping.usage"), GamePermission.PLAYER);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		GameAPI.i18n().sendMessage(sender, "commands.ping.pong");
		return true;
	}
}
package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class HubCommand extends AbstractCommand {
	
	public HubCommand() {
		super("hub", new TranslatableString("commands.hub.usage"), GamePermission.PLAYER);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
	
		BadblockPlayer player = (BadblockPlayer) sender;
		player.sendPlayer("lobby");
		return true;
	}
}

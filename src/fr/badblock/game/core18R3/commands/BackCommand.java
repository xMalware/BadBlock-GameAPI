package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.players.ingamedata.CommandInGameData;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class BackCommand extends AbstractCommand {
	public BackCommand() {
		super("back", new TranslatableString("commands.back.usage"), GamePermission.MODERATOR, GamePermission.MODERATOR, GamePermission.ADMIN, "bk");
		this.allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer badblockPlayer = (BadblockPlayer) sender;
		CommandInGameData data = badblockPlayer.inGameData(CommandInGameData.class);
		
		if(data.lastLocation == null) {
			badblockPlayer.sendTranslatedMessage("commands.back.nolastlocation");
			return true;
		}
		
		badblockPlayer.teleport(data.lastLocation);
		badblockPlayer.sendTranslatedMessage("commands.back.teleportedtolastlocation");
		return true;
	}
}
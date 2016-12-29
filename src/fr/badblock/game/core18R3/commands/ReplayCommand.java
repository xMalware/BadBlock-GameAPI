package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.sentry.SEntry;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class ReplayCommand extends AbstractCommand {
	
	public ReplayCommand() {
		super("replay", new TranslatableString("commands.replay.usage"), GamePermission.PLAYER);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (!GameAPI.getAPI().getRunType().equals(RunType.GAME)) return true;
		
		BadblockPlayer player = (BadblockPlayer) sender;
		GameAPI.getAPI().getRabbitSpeaker().sendAsyncUTF8Publisher("networkdocker.sentry.join", GameAPI.getGson().toJson(new SEntry(player.getName(), Bukkit.getServerName().split("_")[0], true)), 5000, false);

		return true;
	}
}

package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class AutoReplayCommand extends AbstractCommand {

	public AutoReplayCommand() {
		super("autoreplay", new TranslatableString("commands.autoreplay.usage"), GamePermission.PLAYER);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (!GameAPI.getAPI().getRunType().equals(RunType.GAME)) return true;

		BadblockPlayer player = (BadblockPlayer) sender;

		if (args.length != 1)
		{
			player.sendTranslatedMessage("commands.autoreplay.usage");
			return true;
		}

		String game = args[0];

		if (player.getPlayerData().getReplay() != null && player.getPlayerData().getReplay().contains(game))
		{
			player.getPlayerData().getReplay().remove(game);
			player.saveGameData();
			player.sendTranslatedMessage("commands.autoreplay.removereplay");
		}
		else
		{
			player.getPlayerData().getReplay().add(game);
			player.saveGameData();
			player.sendTranslatedMessage("commands.autoreplay.setreplay");
		}

		return true;
	}
}

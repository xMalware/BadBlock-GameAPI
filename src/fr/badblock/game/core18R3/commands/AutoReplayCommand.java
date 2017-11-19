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

		if (args.length != 2)
		{
			player.sendTranslatedMessage("commands.autoreplay.usage");
			return true;
		}

		String type = args[0];
		String game = args[1];
		
		switch (type)
		{
		case "on":
			if (player.getPlayerData().getReplay() != null && player.getPlayerData().getReplay().contains(game))
			{
				player.getPlayerData().getReplay().remove(type);
				player.saveGameData();
				player.sendTranslatedMessage("commands.autoreplay.removereplay");
			}
			else
			{
				player.sendTranslatedMessage("commands.autoreplay.unknownreplay");
			}
			break;
		case "off":
			if (player.getPlayerData().getReplay() == null || !player.getPlayerData().getReplay().contains(game))
			{
				player.getPlayerData().getReplay().add(type);
				player.saveGameData();
				player.sendTranslatedMessage("commands.autoreplay.setreplay");
			}
			else
			{
				player.sendTranslatedMessage("commands.autoreplay.already");
			}
			break;
		default:
			player.sendTranslatedMessage("commands.autoreplay.usage");
			break;
		}

		return true;
	}
}

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
		
		if (args.length == 0)
		{
			player.getPlayerData().setReplay("");
			player.sendTranslatedMessage("game.removereplay");
			return true;
		}
		
		player.getPlayerData().setReplay(args[0]);
		player.sendTranslatedMessage("game.setreplay", args[0]);
		
		player.saveGameData();
		
		return true;
	}
}

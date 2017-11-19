package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
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
		
		//BadblockPlayer player = (BadblockPlayer) sender;
		
		// TODO
		
		return true;
	}
}

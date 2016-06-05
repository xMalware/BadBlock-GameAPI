package fr.badblock.game.v1_8_R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class BroadcastCommand extends AbstractCommand {
	public BroadcastCommand() {
		super("broadcast", new TranslatableString("commands.broadcast.usage"), GamePermission.BMODERATOR, "bc");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0)
			return false;
		
		new TranslatableString("commands.broadcast.message", StringUtils.join(args, " ")).broadcast();;
		return true;
	}
}
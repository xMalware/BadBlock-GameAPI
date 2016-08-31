package fr.badblock.game.core18R3.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;

public class MoreCommand extends AbstractCommand {
	public MoreCommand() {
		super("more", new TranslatableString("commands.more.usage"), GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer player = (BadblockPlayer) sender;
		
		if(!ItemStackUtils.isValid(player.getItemInHand())){
			sendTranslatedMessage(player, "commands.more.handempty");
		} else {
			ItemStackUtils.maxStack(player.getItemInHand());
			sendTranslatedMessage(player, "commands.more.make");
		}
		
		return true;
	}
}
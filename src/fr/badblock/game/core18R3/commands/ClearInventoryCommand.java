package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class ClearInventoryCommand extends AbstractCommand {
	public ClearInventoryCommand() {
		super("clearinventory", new TranslatableString("commands.clearinventory.usage"), GamePermission.BMODERATOR, "ci", "clear");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = null;
	
		if(args.length == 0 && !(sender instanceof Player)){
			return false;
		} else if(args.length > 0){
			concerned = (BadblockPlayer) Bukkit.getPlayer(args[0]);
		} else {
			concerned = (BadblockPlayer) sender;
		}

		if(concerned == null){
			new TranslatableString("commands.unknowplayer", args[0]).send(sender);
		} else {
			sendTranslatedMessage(concerned, "commands.clearinventory.cleared");
		
			if(!concerned.equals(sender)){
				sendTranslatedMessage(concerned, "commands.clearinventory.cleared-other", concerned.getName());
			}
		}
		
		return true;
	}
}

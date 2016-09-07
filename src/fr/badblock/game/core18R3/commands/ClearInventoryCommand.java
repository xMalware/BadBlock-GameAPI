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
			if(!concerned.equals(sender)){
				if(!sender.hasPermission(GamePermission.ADMIN.getPermission())){
					sendTranslatedMessage(sender, "commands.nopermission");
					return true;
				}
				
				sendTranslatedMessage(concerned, "commands.clearinventory.cleared-other", concerned.getName());
			}
			
			sendTranslatedMessage(concerned, "commands.clearinventory.cleared");
		
			concerned.clearInventory();
		}
		
		return true;
	}
}

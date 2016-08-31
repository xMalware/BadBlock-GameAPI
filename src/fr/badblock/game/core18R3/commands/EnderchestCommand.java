package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class EnderchestCommand extends AbstractCommand {
	public EnderchestCommand() {
		super("enderchest", new TranslatableString("commands.enderchest.usage"), GamePermission.BMODERATOR, "ec");
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer player	 = (BadblockPlayer) sender;
		BadblockPlayer concerned = null;
	
		if(args.length > 0){
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
			}
			
			player.openInventory(concerned.getEnderChest());
		}
		
		return true;
	}
}
package fr.badblock.game.v1_8_R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.game.v1_8_R3.players.CommandInGameData;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class VanishCommand extends AbstractCommand {
	public VanishCommand() {
		super("vanish", new TranslatableString("commands.vanish.usage"), GamePermission.BMODERATOR, "v");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = null;

		if(args.length == 0){
			if(sender instanceof Player){
				concerned = (BadblockPlayer) sender;
			} else return false;
		} else {
			concerned = (BadblockPlayer) Bukkit.getPlayer(args[0]);
		}
		
		if(concerned == null){
			new TranslatableString("commands.unknowplayer", args[0]).send(sender);
			return true;
		}
		
		boolean vanish = concerned.inGameData(CommandInGameData.class).vanish;
		concerned.inGameData(CommandInGameData.class).vanish = !vanish;
		
		String vanStr = vanish ? "remove" : "set";
		
		concerned.sendTranslatedMessage("commands.vanish." + vanStr);
		
		if(!concerned.equals(sender))
			GameAPI.i18n().sendMessage(sender, "commands.vanish." + vanStr + "-confirm", concerned.getName());
		
		for(Player player : Bukkit.getOnlinePlayers()){
			BadblockPlayer p = (BadblockPlayer) player;
			
			if(!p.hasPermission(GamePermission.BMODERATOR) && !p.equals(concerned)){
				if(!vanish)
					p.hidePlayer(concerned);
				else p.showPlayer(concerned);
			}
		}
		
		return true;
	}
}
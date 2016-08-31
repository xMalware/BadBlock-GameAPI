package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.game.core18R3.players.ingamedata.CommandInGameData;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class GodmodeCommand extends AbstractCommand {
	public GodmodeCommand() {
		super("godmode", new TranslatableString("commands.godmode.usage"), GamePermission.BMODERATOR, "god");
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
		
		boolean godmode = concerned.inGameData(CommandInGameData.class).godmode;
		
		concerned.inGameData(CommandInGameData.class).godmode = !godmode;
		
		String godStr = godmode ? "remove" : "set";
		
		concerned.sendTranslatedMessage("commands.godmode." + godStr);
		
		if(!concerned.equals(sender))
			GameAPI.i18n().sendMessage(sender, "commands.godmode." + godStr + "-confirm", concerned.getName());
		
		return true;
	}
}
package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class FreezeCommand extends AbstractCommand {
	public FreezeCommand() {
		super("freeze", new TranslatableString("commands.freeze.usage"), GamePermission.MODERATOR, GamePermission.MODERATOR, GamePermission.MODERATOR);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0){
			return false;
		}
		
		BadblockPlayer concerned = (BadblockPlayer) Bukkit.getPlayer(args[0]);

		if(concerned == null){
			new TranslatableString("commands.unknowplayer", args[0]).send(sender);
		} else {
			if (!sender.hasPermission(GamePermission.BMODERATOR.getPermission()) && sender.equals(concerned)) {
				new TranslatableString("commands.youcantdothistoyou", args[0]).send(sender);		
				return true;
			}
			if (!sender.hasPermission(GamePermission.BMODERATOR.getPermission()) && concerned.hasPermission(GamePermission.MODERATOR.getPermission())) {
				new TranslatableString("commands.freeze.insufficientpermission", args[0]).send(sender);
				return true;
			}
			String type = concerned.isJailed() ? "un" : "";
			
			if(concerned.isJailed()){
				concerned.jailPlayerAt(null);
			} else concerned.jailPlayerAt(concerned.getLocation());
			
			concerned.sendTranslatedMessage("commands.freeze." + type + "jailed");
			new TranslatableString("commands.freeze." + type + "jail").send(sender);
		}

		return true;
	}
}
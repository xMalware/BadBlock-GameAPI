package fr.badblock.game.v1_8_R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class FlyCommand extends AbstractCommand {
	public FlyCommand() {
		super("fly", new TranslatableString("commands.fly.usage"), GamePermission.MODERATOR);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Player concerned = null;
		if(args.length == 0 && !(sender instanceof Player)){
			return false;
		} else if(args.length > 0){
			concerned = Bukkit.getPlayer(args[0]);
		} else {
			concerned = (Player) sender;
		}

		if(concerned == null){
			new TranslatableString("commands.unknowplayer", args[0]).send(sender);
		} else {
			boolean result = true;
			String  key	   = "allowed";

			if(concerned.getAllowFlight()){
				result = false;
				key    = "disallowed";
			}

			concerned.setAllowFlight(result);
			concerned.setFlying(result);

			new TranslatableString("commands.fly." + key).send(concerned);

			if(args.length > 0){
				new TranslatableString("commands.fly." + key + "-success", concerned.getName()).send(sender);
			}
		}
		
		return true;
	}
}

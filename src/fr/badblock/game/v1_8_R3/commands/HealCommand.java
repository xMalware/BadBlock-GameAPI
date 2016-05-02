package fr.badblock.game.v1_8_R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class HealCommand extends AbstractCommand {
	public HealCommand() {
		super("heal", new TranslatableString("commands.heal.usage"), GamePermission.ADMIN);
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
			concerned.heal();
			
			new TranslatableString("commands.heal.healed").send(sender);
			
			if(args.length > 0){
				new TranslatableString("commands.heal.healed-success", concerned.getName()).send(sender);
			}
		}
		
		return true;
	}
}

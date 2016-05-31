package fr.badblock.game.v1_8_R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.achievements.AchievementList;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

/**
 * Commande de GameMode
 * @author LeLanN
 */
public class GameModeCommand extends AbstractCommand {
	
	public GameModeCommand() {
		super("gamemode", new TranslatableString("commands.gamemode.usage"), GamePermission.ADMIN, "gm");
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		Player concerned;
		if((args.length == 1 && !(sender instanceof Player)) || args.length == 0){
			return false;
		} else if(args.length > 1) {
			concerned = Bukkit.getPlayer(args[1]);
		} else {
			concerned = (Player) sender;
		}

		if(concerned == null){
			new TranslatableString("commands.unknowplayer", args[1]).send(sender);
		} else {
			if(args[0].equalsIgnoreCase("yolo")){
				BadblockPlayer player = (BadblockPlayer) concerned;
				player.changePlayerDimension(Environment.THE_END);
				
				return true;
			} else if(args[0].equalsIgnoreCase("yolo2")){
				BadblockPlayer player = (BadblockPlayer) concerned;
				AchievementList.openInventory(player, "rush");
				
				return true;
			}
			
			GameMode gm = matchGameMode(args[0]);
			
			if(gm == null){
				new TranslatableString("commands.gamemode.unknowmode", args[0]).send(sender);
			} else {
				concerned.setGameMode(gm);

				new TranslatableString("commands.gamemode.change", gm.toString()).send(concerned);

				if(args.length > 1){
					new TranslatableString("commands.gamemode.change-success", args[1], gm.toString()).send(sender);
				}
			}
		}
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public GameMode matchGameMode(String arg){
		for(GameMode gm : GameMode.values()){
			if(arg.equals(String.valueOf(gm.getValue())) || arg.equalsIgnoreCase(gm.toString())){
				return gm;
			}
		}
		return null;
	}
}

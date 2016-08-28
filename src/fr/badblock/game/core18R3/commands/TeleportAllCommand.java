package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class TeleportAllCommand extends AbstractCommand {
	public TeleportAllCommand() {
		super("teleportall", new TranslatableString("commands.teleportall.usage"), GamePermission.BMODERATOR, "tpall");
		this.allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = (BadblockPlayer) sender;
		Bukkit.getOnlinePlayers().forEach(player -> {
			BadblockPlayer badblockPlayer = (BadblockPlayer) player;
			badblockPlayer.teleport(concerned);
			GameAPI.i18n().sendMessage(sender, "commands.teleport.to", concerned.getName());
		});
		GameAPI.i18n().sendMessage(sender, "commands.teleport.to-other", concerned.getTranslatedMessage("commands.teleport.all"), concerned.getName());
		return true;
	}
	
	private boolean tppos(CommandSender sender, String[] args){
		BadblockPlayer player = null;
		
		int begin = 0;
		
		if(args.length == 4){
			begin = 1;
			player = (BadblockPlayer) Bukkit.getPlayer(args[0]);
		} else if(!(sender instanceof Player)){
			return false;
		} else player = (BadblockPlayer) sender;
		
		if(player == null){
			new TranslatableString("commands.unknowplayer", args[0]).send(sender); return true;
		}
		
		Double x = get(args[begin], player.getLocation().getX(), sender);
		if(x == null) return true;
		
		Double y = get(args[begin + 1], player.getLocation().getY(), sender);
		if(y == null) return true;
		
		Double z = get(args[begin + 2], player.getLocation().getZ(), sender);
		if(z == null) return true;
		
		player.teleport(new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch()));
		
		if(player.equals(sender)){
			GameAPI.i18n().sendMessage(sender, "commands.teleport.place");
		} else {
			GameAPI.i18n().sendMessage(sender, "commands.teleport.place-other", player.getName());
		}

		return true;
	}
	
	private Double get(String arg, double base, CommandSender sender){
		boolean rel = false;
		
		if(arg.startsWith("~")){
			rel = true;
			arg = arg.substring(1);
			
			if(arg.length() == 0)
				arg = "0";
		}
		
		Double parsed = null;
		
		try {
			parsed = Double.parseDouble(arg);
		} catch(Exception e){
			new TranslatableString("commands.nan", arg).send(sender); return null;
		}
		
		if(rel)
			parsed += base;
		
		return parsed;
	}
}
package fr.badblock.game.core18R3.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class TimeCommand extends AbstractCommand {
	public static final long DAY   = 1_000L;
	public static final long NIGHT = 14_000L;
	
	public TimeCommand() {
		super("time", new TranslatableString("commands.time.usage"), GamePermission.BMODERATOR);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0)
			return false;
		
		
		switch(args[0].toLowerCase()){
			case "set":
				if(args.length == 1){
					return false;
				}
				
				args[1] = args[1].toLowerCase();
				
				long time = 0;
				
				if(args[1].equalsIgnoreCase("day")){
					time = DAY;
				} else if(args[1].equalsIgnoreCase("night")){
					time = NIGHT;
				} else {
					try {
						time = Integer.parseInt(args[1]);
					} catch(Exception e){
						sendTranslatedMessage(sender, "commands.nan", args[1]);
						return true;
					}
				}
				
				List<World> worlds = getWorlds(sender, args, 2);
				
				if(worlds == null)
					return true;
				
				for(World world : worlds){
					world.setTime(time);
					sendTranslatedMessage(sender, "commands.time.set", world.getName(), time);
				}
			break;
			case "view":
				worlds = getWorlds(sender, args, 1);
				
				if(worlds == null)
					return true;
			
				for(World world : worlds){
					long   hour = world.getTime() / 1000L;
					double mins = (double) (world.getTime() % 1000L);
					
					mins /= 1000d;
					mins *= 60d;
					
					String result = "";
					
					if(hour < 10)
						result += "0";
					result += hour;
					
					if(mins < 10)
						result += "0";
					result += mins;
					
					sendTranslatedMessage(sender, "commands.time.view", world.getName(), world.getTime(), result);
				}
					
			break;
		}
		
		
		return true;
	}
	
	private List<World> getWorlds(CommandSender sender, String[] args, int first){
		List<World> worlds = null;
		
		if(args.length < first){
			if(sender instanceof Player){
				Arrays.asList( ((Player) sender).getWorld() );
			} else {
				worlds = Bukkit.getWorlds();
			}
		} else {
			
			if(args[first].equalsIgnoreCase("all")){
				worlds = Bukkit.getWorlds();
			} else {
				World world = Bukkit.getWorld(args[2]);
				
				if(world == null){
					sendTranslatedMessage(sender, "commands.time.unknowworld", args[first]);
					return null;
				}
				
				worlds = Arrays.asList( world );
			}
			
		}
		
		return worlds;
	}
}
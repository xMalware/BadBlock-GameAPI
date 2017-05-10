package fr.badblock.game.core18R3.commands.we;

import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import fr.badblock.game.core18R3.worldedit.WorldEditThread;
import fr.badblock.game.core18R3.worldedit.actions.WEActionReplace;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.worldedit.WEBlockIterator;
import net.minecraft.server.v1_8_R3.Item;

public class ReplaceCommand extends SelectionNeededCommand {
	public ReplaceCommand() {
		super("replace");
	}

	@Override
	protected boolean exec(BadblockPlayer concerned, String[] args) {
		if(args.length <= 1)
			return false;
		
		Material replaceMat = null;
		byte replaceData = -1;
		
		if(!args[0].equalsIgnoreCase("all"))
		{
			String[] splitted = SetCommand.splitItem(args[0]);
			
			replaceMat = SetCommand.getItem(splitted[0]);
			
			if(replaceMat == null) {
				sendTranslatedMessage(concerned, "commands.give.unknow-type", splitted[0]);
				return true;
			}
	
			try {
				replaceData = Byte.parseByte(splitted[1]);
			} catch (Exception e) {
				sendTranslatedMessage(concerned, "commands.nan", splitted[1]);
				return true;
			}
			
			if(!SetCommand.isValidBlock(replaceMat, replaceData))
			{
				sendTranslatedMessage(concerned, "commands.worldedit.invalidblock", splitted[0] + ":" + splitted[1]);
				return true;
			}
		}
		
		String[] splitted = SetCommand.splitItem(args[1]);
		
		Material material = SetCommand.getItem(splitted[0]);
		byte data = 0;
		
		if(material == null) {
			sendTranslatedMessage(concerned, "commands.give.unknow-type", splitted[0]);
			return true;
		}

		try {
			data = Byte.parseByte(splitted[1]);
		} catch (Exception e) {
			sendTranslatedMessage(concerned, "commands.nan", splitted[1]);
			return true;
		}
		
		if(!SetCommand.isValidBlock(material, data))
		{
			sendTranslatedMessage(concerned, "commands.worldedit.invalidblock", splitted[0] + ":" + splitted[1]);
			return true;
		}
		
		WEBlockIterator iterator = SetCommand.getIterator(concerned, args.length > 2 ? args[2] : null);
		
		if(iterator == null)
			return true;
		
		sendTranslatedMessage(concerned, "commands.worldedit.blockcount", SetCommand.formatLong(iterator.getCount()));
		WorldEditThread.thread.getAction().addActions( new WEActionReplace(iterator, concerned, material, data == -1 ? 0 : data, replaceMat, replaceData) );
		
		return true;
	}
	
	@Override
	public Collection<String> doTab(CommandSender sender, String[] args) {
		if(args.length == 1){
			args[0] = args[0].toLowerCase();

			if(!args[0].startsWith("minecraft:"))
				args[0] = "minecraft:" + args[0];

			return Item.REGISTRY.keySet().stream().map(mcKey -> {
				return mcKey.toString();
			}).collect(Collectors.toList());
		} else if(args.length == 2){
			args[1] = args[1].toLowerCase();

			if(!args[1].startsWith("minecraft:"))
				args[1] = "minecraft:" + args[1];

			return Item.REGISTRY.keySet().stream().map(mcKey -> {
				return mcKey.toString();
			}).collect(Collectors.toList());
		}
		else if(args.length == 3)
		{
			args[2] = args[2].toLowerCase();
			return SetCommand.shapes.keySet().stream().collect(Collectors.toList());
		}
		
		return null;
	}

	@Override
	public String[] changeArgs(CommandSender sender, String[] args){
		if(args.length == 2){
			args[1] = args[1].toLowerCase();

			if(!args[1].startsWith("minecraft:"))
				args[1] = "minecraft:" + args[1];
		}
		
		return args;
	}
}
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
		
		String[] splitted = SetCommand.splitItem(args[0]);
		
		Material replaceMat = SetCommand.getItem(splitted[0]);
		byte replaceData = 0;
		
		if(replaceMat == null) {
			sendTranslatedMessage(concerned, "commands.give.unknow-type", args[0]);
			return true;
		}

		try {
			replaceData = Byte.parseByte(splitted[1]);
		} catch (Exception e) {
			sendTranslatedMessage(concerned, "commands.nan", args[1]);
			return true;
		}
		
		if(!SetCommand.isValidBlock(replaceMat, replaceData))
		{
			sendTranslatedMessage(concerned, "commands.worldedit.invalidblock", splitted[0] + ":" + splitted[1]);
			return true;
		}
		
		splitted = SetCommand.splitItem(args[1]);
		
		Material material = SetCommand.getItem(splitted[0]);
		byte data = 0;
		
		if(material == null) {
			sendTranslatedMessage(concerned, "commands.give.unknow-type", args[0]);
			return true;
		}

		try {
			data = Byte.parseByte(splitted[1]);
		} catch (Exception e) {
			sendTranslatedMessage(concerned, "commands.nan", args[1]);
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
		
		sendTranslatedMessage(concerned, "commands.worldedit.blockcount", iterator.getCount());
		WorldEditThread.thread.getAction().addActions( new WEActionReplace(iterator, concerned, material, data, replaceMat, replaceData) );
		
		return true;
	}
	
	@Override
	public Collection<String> doTab(CommandSender sender, String[] args) {
		if(args.length == 1){
			return super.doTab(sender, args);
		} else if(args.length == 2){
			args[1] = args[1].toLowerCase();

			if(!args[1].startsWith("minecraft:"))
				args[1] = "minecraft:" + args[1];

			return Item.REGISTRY.keySet().stream().map(mcKey -> {
				return mcKey.toString();
			}).collect(Collectors.toList());
		} else if(args.length == 3){
			args[2] = args[2].toLowerCase();

			if(!args[2].startsWith("minecraft:"))
				args[2] = "minecraft:" + args[2];

			return Item.REGISTRY.keySet().stream().map(mcKey -> {
				return mcKey.toString();
			}).collect(Collectors.toList());
		}
		else if(args.length == 4)
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
package fr.badblock.game.core18R3.commands.we;

import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import fr.badblock.game.core18R3.worldedit.WorldEditThread;
import fr.badblock.game.core18R3.worldedit.actions.WEActionSet;
import fr.badblock.game.core18R3.worldedit.iterators.WELineIterator;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.worldedit.WEBlockIterator;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftKey;

public class SetCommand extends SelectionNeededCommand {
	public SetCommand() {
		super("set");
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean exec(BadblockPlayer concerned, String[] args) {
		if(args.length == 0)
			return false;
		
		Material material = Material.matchMaterial(args[0]);
		byte data = 0;

		if(material == null) {
			Item nmsItem = Item.REGISTRY.get(new MinecraftKey(args[0]));

			if(nmsItem != null) {
				net.minecraft.server.v1_8_R3.ItemStack localItemStack = new net.minecraft.server.v1_8_R3.ItemStack(nmsItem, 1);
				material = CraftItemStack.asBukkitCopy(localItemStack).getType();
			}
		}
		
		if(material == null) {
			sendTranslatedMessage(concerned, "commands.give.unknow-type", args[0]);
			return true;
		}

		try {
			if(args.length > 1) {
				data = Byte.parseByte(args[1]);
			}
		} catch (Exception e) {
			sendTranslatedMessage(concerned, "commands.nan", args[1]);
			return true;
		}
		
		if(Block.d.a( (material.getId() << 4) | data ) == null)
		{
			sendTranslatedMessage(concerned, "commands.worldedit.invalidblock", args[0], args.length == 1 ? 0 : args[1]);
			return true;
		}
		
		WEBlockIterator iterator = new WELineIterator(concerned.getSelection());
		WEActionSet action = new WEActionSet(iterator, concerned, material, data);
		WorldEditThread thread = new WorldEditThread();
		
		sendTranslatedMessage(concerned, "commands.worldedit.blockcount", iterator.getCount());
		
		thread.runTaskTimer(GameAPI.getAPI(), 0, 1L);
		thread.getAction().addActions(action);
		
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
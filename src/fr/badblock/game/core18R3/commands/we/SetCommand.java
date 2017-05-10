package fr.badblock.game.core18R3.commands.we;

import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import fr.badblock.game.core18R3.worldedit.WorldEditThread;
import fr.badblock.game.core18R3.worldedit.actions.WEActionSet;
import fr.badblock.game.core18R3.worldedit.iterators.WECuboidIterator;
import fr.badblock.game.core18R3.worldedit.iterators.WECylinderIterator;
import fr.badblock.game.core18R3.worldedit.iterators.WEEllipsoidIterator;
import fr.badblock.game.core18R3.worldedit.iterators.WEEmptyCuboidIterator;
import fr.badblock.game.core18R3.worldedit.iterators.WEEmptyCylinderIterator;
import fr.badblock.game.core18R3.worldedit.iterators.WEEmptyEllipsoidIterator;
import fr.badblock.game.core18R3.worldedit.iterators.WELineIterator;
import fr.badblock.game.core18R3.worldedit.iterators.WEWallsIterator;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.selections.CuboidSelection;
import fr.badblock.gameapi.worldedit.WEBlockIterator;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftKey;

public class SetCommand extends SelectionNeededCommand {
	public final static Map<String, Class<?>> shapes;
	public final static NumberFormat formatter = NumberFormat.getNumberInstance(Locale.FRENCH);
	
	static
	{
		shapes = new HashMap<>();
		shapes.put("cuboid", WECuboidIterator.class);
		shapes.put("hcuboid", WEEmptyCuboidIterator.class);
		shapes.put("walls", WEWallsIterator.class);
		shapes.put("sphere", WEEllipsoidIterator.class);
		shapes.put("hsphere", WEEmptyEllipsoidIterator.class);
		shapes.put("cyl", WECylinderIterator.class);
		shapes.put("hcyl", WEEmptyCylinderIterator.class);
		shapes.put("line", WELineIterator.class);
	}
	
	public static String formatLong(long value)
	{
		return formatter.format(value);
	}
	
	public static String[] splitItem(String item)
	{
		String[] splitted = item.split(":");
		String resMaterial = "";
		String resData = "0";
		
		int i = 0;
		
		if(splitted[i].equalsIgnoreCase("minecraft"))
		{
			i++;
			resMaterial = "minecraft:";
		}
		
		if(i < splitted.length)
			resMaterial += splitted[i++];
		
		if(i < splitted.length)
			resData = splitted[i++];
		
		return new String[]{resMaterial, resData};
	}
	
	public static Material getItem(String from)
	{
		Material material = Material.matchMaterial(from);
		
		if(material == null) {
			Item nmsItem = Item.REGISTRY.get(new MinecraftKey(from));

			if(nmsItem != null) {
				net.minecraft.server.v1_8_R3.ItemStack localItemStack = new net.minecraft.server.v1_8_R3.ItemStack(nmsItem, 1);
				material = CraftItemStack.asBukkitCopy(localItemStack).getType();
			}
		}
		
		return material;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isValidBlock(Material material, byte data)
	{
		return Block.d.a( (material.getId() << 4) | data ) != null;
	}
	
	public static WEBlockIterator getIterator(BadblockPlayer player, String shape)
	{
		if(shape == null)
			return new WECuboidIterator(player.getSelection());
		
		shape = shape.toLowerCase();
		
		if(!shapes.containsKey(shape))
		{
			player.sendTranslatedMessage("commands.worldedit.invalidshape", shape, StringUtils.join(shapes.keySet(), "&b, ", "&7, ") );
			return null;
		}
		
		try {
			return (WEBlockIterator)shapes.get(shape).getConstructor(CuboidSelection.class).newInstance(player.getSelection());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public SetCommand() {
		super("set");
	}

	@Override
	protected boolean exec(BadblockPlayer concerned, String[] args) {
		if(args.length == 0)
			return false;
		
		String[] splitted = splitItem(args[0]);
		
		Material material = getItem(splitted[0]);
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
		
		if(!isValidBlock(material, data))
		{
			sendTranslatedMessage(concerned, "commands.worldedit.invalidblock", splitted[0] + ":" + splitted[1]);
			return true;
		}
		
		WEBlockIterator iterator = getIterator(concerned, args.length > 1 ? args[1] : null);
		
		if(iterator == null)
			return true;
		
		sendTranslatedMessage(concerned, "commands.worldedit.blockcount", formatLong(iterator.getCount()));
		WorldEditThread.thread.getAction().addActions( new WEActionSet(iterator, concerned, material, data) );
		
		return true;
	}
	
	@Override
	public Collection<String> doTab(CommandSender sender, String[] args) {
		if(args.length == 1){
			args[0] = args[0].toLowerCase();

			if(!args[0].startsWith("minecraft:"))
				args[0] = "minecraft:" + args[1];

			return Item.REGISTRY.keySet().stream().map(mcKey -> {
				return mcKey.toString();
			}).collect(Collectors.toList());
		}
		else if(args.length == 2)
		{
			args[1] = args[1].toLowerCase();
			return shapes.keySet().stream().collect(Collectors.toList());
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
package fr.badblock.game.core18R3.commands;

import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.i18n.Word.WordDeterminant;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.MinecraftKey;

public class GiveCommand extends AbstractCommand {
	public GiveCommand() {
		super("give", new TranslatableString("commands.give.usage"), GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length < 2)
			return false;

		BadblockPlayer concerned = null;

		if(args.length == 0) {
			return false;
		} else {
			concerned = (BadblockPlayer) Bukkit.getPlayer(args[0]);
		}

		if(concerned == null) {
			sendTranslatedMessage(sender, "commands.unknowplayer", args[0]);
			return true;
		}

		Material material = Material.matchMaterial(args[1]);

		if(material == null) {
			Item nmsItem = Item.REGISTRY.get(new MinecraftKey(args[1]));

			if(nmsItem != null) {
				net.minecraft.server.v1_8_R3.ItemStack localItemStack = new net.minecraft.server.v1_8_R3.ItemStack(nmsItem, 1);
				material = CraftItemStack.asBukkitCopy(localItemStack).getType();
			}
		}

		if(material == null) {
			sendTranslatedMessage(sender, "commands.give.unknow-type", args[1]);
			return true;
		}

		int amount = 1;

		try {
			if(args.length > 2) {
				amount = Integer.parseInt(args[2]);
			}
		} catch (Exception e) {
			sendTranslatedMessage(sender, "commands.nan", args[2]);
		}

		short data = 0;

		try {
			if(args.length > 3) {
				data = Short.parseShort(args[3]);
			}
		} catch (Exception e) {
			sendTranslatedMessage(sender, "commands.nan", args[3]);
		}

		ItemStack result = new ItemStack(material, amount, data);

		int notGived = concerned.getInventory().addItem(result).values().stream().mapToInt(item -> {
			return item.getAmount();
		}).sum();

		if(notGived != 0) {
			sendTranslatedMessage(sender, "commands.give.notgived",
					GameMessages.material(material, notGived > 1, WordDeterminant.SIMPLE), notGived, amount - notGived);
		} else {
			sendTranslatedMessage(sender, "commands.give.gived",
					GameMessages.material(material, notGived > 1, WordDeterminant.SIMPLE), amount);
		}

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
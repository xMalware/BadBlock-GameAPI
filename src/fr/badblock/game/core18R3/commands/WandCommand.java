package fr.badblock.game.core18R3.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class WandCommand extends AbstractCommand {
	public WandCommand() {
		super("/wand", new TranslatableString("commands.wand.usage"), GamePermission.ADMIN, "/batondeblaze");
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer concerned = (BadblockPlayer) sender;

		concerned.getInventory().addItem(new ItemStack(Material.BLAZE_ROD, 1));
		concerned.sendMessage(GameAPI.i18n().replaceColors("&7Wi-aah!"));
		
		return true;
	}
}
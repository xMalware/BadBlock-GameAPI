package fr.badblock.game.core18R3.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class SkullCommand extends AbstractCommand {
	public SkullCommand() {
		super("skull", new TranslatableString("commands.skull.usage"), GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length == 0) return false;
		BadblockPlayer concerned = (BadblockPlayer) sender;

		concerned.getInventory().addItem(GameAPI.getAPI().createItemStackFactory().type(Material.SKULL_ITEM)
												 .durability((short) 3)
												 .asSkull(1, args[0]));
		concerned.sendTranslatedMessage("commands.skull.success");
		return true;
	}
}
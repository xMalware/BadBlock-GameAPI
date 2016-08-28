package fr.badblock.game.core18R3.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class WorldsCommand extends AbstractCommand {
	
	public WorldsCommand() {
		super("worlds", new TranslatableString("commands.worlds.usage"), GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length != 0) return true;
		List<String> strings = new ArrayList<>();
		Bukkit.getWorlds().forEach(world -> strings.add(GameAPI.i18n().get(sender, "commands.worlds.eachmessage", world.getName())[0]));
		GameAPI.i18n().sendMessage(sender, "commands.worlds.displayer", strings);
		return true;
	}
	
}
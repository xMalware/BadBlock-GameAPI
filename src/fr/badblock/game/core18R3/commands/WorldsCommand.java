package fr.badblock.game.core18R3.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class WorldsCommand extends AbstractCommand {
	public WorldsCommand() {
		super("worlds", new TranslatableString("commands.worlds.usage"), GamePermission.ADMIN, GamePermission.ADMIN, GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length != 0)
			return true;
		
		List<String> strings = Bukkit.getWorlds().stream().map(world -> world.getName()).collect(Collectors.toList());
		
		GameAPI.i18n().sendMessage(sender, "commands.worlds.displayer", StringUtils.join(strings, "&b, ", "&7, "));
		return true;
	}
}
package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class WorldCommand extends AbstractCommand {
	public WorldCommand() {
		super("world", new TranslatableString("commands.world.usage"), GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if(args.length < 1)
			return false;
		
		World world = Bukkit.getWorld(args[0]);
		
		if(world == null) {
			GameAPI.i18n().sendMessage(sender, "commands.unknowworld", args[0]);
			return true;
		}
		
		if(world.getSpawnLocation() == null) {
			GameAPI.i18n().sendMessage(sender, "commands.world.unknownspawnlocation", world.getName());
			return true;
		}
		
		BadblockPlayer badblockPlayer = (BadblockPlayer) sender;
		badblockPlayer.teleport(world.getSpawnLocation());
		
		sendTranslatedMessage(sender, "commands.world.teleported-to", world.getName());
		return true;
	}
}
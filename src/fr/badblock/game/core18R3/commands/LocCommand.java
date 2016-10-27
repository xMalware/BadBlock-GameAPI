package fr.badblock.game.core18R3.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class LocCommand extends AbstractCommand {

	public LocCommand() {
		super("loc", new TranslatableString("hub.loc"), GamePermission.ADMIN, GamePermission.ADMIN, GamePermission.ADMIN);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer player = (BadblockPlayer) sender;
		Location location = player.getLocation();
		player.sendMessage("--------------------------");
		player.sendMessage("World: " + location.getWorld().getName());
		player.sendMessage("X: " + location.getX());
		player.sendMessage("Y: " + location.getY());
		player.sendMessage("Z: " + location.getZ());
		player.sendMessage("Pitch: " + location.getPitch());
		player.sendMessage("Yaw: " + location.getYaw());
		player.sendMessage("--------------------------");
		return true;
	}
}

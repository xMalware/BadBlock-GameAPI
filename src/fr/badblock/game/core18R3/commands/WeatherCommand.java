package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class WeatherCommand extends AbstractCommand {

	public WeatherCommand() {
		super("weather", new TranslatableString("commands.weather.usage"), GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0)
			return false;
		World defaultWorld = null;
		switch (args.length) {
		case 1:
			defaultWorld = sender instanceof BadblockPlayer ? Bukkit.getWorlds().get(0)
					: ((BadblockPlayer) sender).getWorld();
			break;
		case 2:
			defaultWorld = Bukkit.getWorld(args[1]);
			break;
		}
		if (defaultWorld == null) {
			GameAPI.i18n().sendMessage(sender, "commands.weather.unknownworld");
			return true;
		}
		String type = args[0];
		if (type.equalsIgnoreCase("sun")) {
			if (defaultWorld.isThundering() || defaultWorld.hasStorm()) {
				defaultWorld.setThundering(false);
				defaultWorld.setWeatherDuration(0);
				GameAPI.i18n().sendMessage(sender, "commands.weather.nowsunny");
			} else {
				GameAPI.i18n().sendMessage(sender, "commands.weather.alreadysunny");
				return true;
			}
		} else if (type.equalsIgnoreCase("rain") || type.equalsIgnoreCase("storm")) {
			if (!defaultWorld.hasStorm()) {
				defaultWorld.setStorm(true);
				defaultWorld.setWeatherDuration(Integer.MAX_VALUE);
				GameAPI.i18n().sendMessage(sender, "commands.weather.nowrainy");
			} else {
				GameAPI.i18n().sendMessage(sender, "commands.weather.alreadyrainy");
				return true;
			}
		} else {
			return false;
		}
		return true;
	}
}
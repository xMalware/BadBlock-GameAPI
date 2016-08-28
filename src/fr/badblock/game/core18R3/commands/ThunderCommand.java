package fr.badblock.game.core18R3.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class ThunderCommand extends AbstractCommand {

	public ThunderCommand() {
		super("thunder", new TranslatableString("commands.thunder.usage"), GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0) return false;
		World defaultWorld = null;
		switch (args.length) {
		case 1:
			defaultWorld = sender instanceof BadblockPlayer ? Bukkit.getWorlds().get(0) : ((BadblockPlayer) sender).getWorld();
			break;
		case 2:
			defaultWorld = Bukkit.getWorld(args[1]);
			break;
		}
		if (defaultWorld == null) {
			GameAPI.i18n().sendMessage(sender, "commands.thunder.unknownworld");
			return true;
		}
		String type = args[0];
		if (type.equalsIgnoreCase("true")) {
			if (!defaultWorld.isThundering()) {
				defaultWorld.setThundering(true);
				defaultWorld.setThunderDuration(Integer.MAX_VALUE);
				GameAPI.i18n().sendMessage(sender, "commands.thunder.true");
			}else{
				GameAPI.i18n().sendMessage(sender, "commands.thunder.alreadythundering");
				return true;
			}
		}else if (type.equalsIgnoreCase("false")) {
			if (defaultWorld.isThundering()) {
				defaultWorld.setThundering(false);
				defaultWorld.setThunderDuration(0);
				GameAPI.i18n().sendMessage(sender, "commands.thunder.false");
			}else{
				GameAPI.i18n().sendMessage(sender, "commands.thunder.alreadynotthundering");
				return true;
			}
		}else{
			return false;
		}
		return true;
	}
}
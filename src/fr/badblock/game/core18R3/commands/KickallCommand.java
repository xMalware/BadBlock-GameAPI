package fr.badblock.game.core18R3.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class KickallCommand extends AbstractCommand {
	public KickallCommand() {
		super("kickall", new TranslatableString("commands.kickall.usage"), GamePermission.ADMIN);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		if (args.length == 0) {
			return false;
		}

		UUID uniqueId = (sender instanceof Player) ? ((Player) sender).getUniqueId() : null;
		String server = args[0].toLowerCase();

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!player.getUniqueId().equals(uniqueId)) {
				BadblockPlayer bbPlayer = (BadblockPlayer) player;
				bbPlayer.sendPlayer(server);
			}
		}

		return true;
	}
}
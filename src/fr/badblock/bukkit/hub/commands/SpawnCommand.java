package fr.badblock.bukkit.hub.commands;

import org.bukkit.command.CommandSender;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.listeners.PlayerJoinListener;
import fr.badblock.gameapi.command.AbstractCommand;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.ConfigUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class SpawnCommand extends AbstractCommand {
	public SpawnCommand() {
		super("spawn", new TranslatableString("hub.spawn.help"), GamePermission.PLAYER, GamePermission.PLAYER, GamePermission.PLAYER);
		allowConsole(false);
	}

	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		BadblockPlayer player = (BadblockPlayer) sender;
		player.sendMessage(player.getLocation().getX() + " / " + player.getLocation().getZ());
		player.sendTranslatedMessage("hub.admin.spawnteleporting.loading");
		PlayerJoinListener.reload(player);
		player.teleport(ConfigUtils.getLocation(BadBlockHub.getInstance(), "worldspawn"));
		return true;
	}
}

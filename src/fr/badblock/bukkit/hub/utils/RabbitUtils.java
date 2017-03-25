package fr.badblock.bukkit.hub.utils;

import org.bukkit.entity.Player;

import fr.badblock.actions.CommandFactory;
import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.rabbitconnector.RabbitService;
import fr.badblock.utils.Encodage;

public class RabbitUtils {

	public static void forceCommand(Player player, String command) {
		// Build a CommandFactory
		CommandFactory commandFactory = new CommandFactory(player.getName(), command);
		BadBlockHub hub = BadBlockHub.getInstance();
		RabbitService rabbitService = hub.getRabbitService();
		rabbitService.sendAsyncPacket("forcecommand.ladder", hub.getGson().toJson(commandFactory), Encodage.UTF8,
				RabbitPacketType.PUBLISHER, 10000, false);
	}

}

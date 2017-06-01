package fr.badblock.game.core18R3.technologies.rabbitlisteners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class VanishTeleportListener extends RabbitListener {

	public static Map<String, Long> time = new HashMap<>();
	public static Map<String, String[]> splitters = new HashMap<>();

	public VanishTeleportListener() {
		super(RabbitConnector.getInstance().getService("default"), "vanishTeleport", true, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		String[] splitter = body.split(";");
		BadblockPlayer player = BukkitUtils.getPlayer(splitter[0]);
		if (player == null) {
			time.put(splitter[0].toLowerCase(), System.currentTimeMillis() + 10000);
			splitters.put(splitter[0].toLowerCase(), splitter);
		}
		else {
			manage(player, splitter);
		}
	}

	public static void manage(BadblockPlayer player, String[] splitter) {
		if (player == null) return;
		if (GameAPI.getAPI().getRunType().equals(RunType.LOBBY)) return;
		player.closeInventory();
		player.getInventory().addItem(new ItemStack(Material.FISHING_ROD));
		player.setBadblockMode(BadblockMode.SPECTATOR);
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(true);
		player.setFlying(true);
		if (splitter == null) return;
		if (splitter.length > 1 && splitter[1] != null && !splitter[1].isEmpty()) {
			BadblockPlayer otherPlayer = BukkitUtils.getPlayer(splitter[1]);
			if (otherPlayer != null) {
				player.teleport(otherPlayer);
			}
		}
	}

}

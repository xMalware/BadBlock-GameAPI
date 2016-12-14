package fr.badblock.game.core18R3.technologies.rabbitlisteners;

import org.bukkit.GameMode;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class VanishTeleportListener extends RabbitListener {

	public VanishTeleportListener() {
		super(RabbitConnector.getInstance().getService("default"), "vanishTeleport", true, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		String[] splitter = body.split(";");
		BadblockPlayer player = BukkitUtils.getPlayer(splitter[0]);
		if (player == null) System.out.println("No player connected: " + splitter[0]);
		else {
			TaskManager.runTaskLater(new Runnable() {
				@Override
				public void run() {
					player.sendTranslatedMessage("game.youjoinedinvanish");
					player.closeInventory();
					player.setBadblockMode(BadblockMode.SPECTATOR);
					player.setGameMode(GameMode.SPECTATOR);
					player.setVisible(false, player -> !player.hasPermission("game.seeothermodo") && !player.getBadblockMode().equals(BadblockMode.SPECTATOR));
					player.setVisible(true, player -> player.hasPermission("game.seeothermodo") && player.getBadblockMode().equals(BadblockMode.SPECTATOR));
					if (splitter.length > 1 && splitter[1] != null && !splitter[1].isEmpty()) {
						BadblockPlayer otherPlayer = BukkitUtils.getPlayer(splitter[1]);
						if (otherPlayer != null) {
							player.teleport(otherPlayer);
						}
					}
				}
			}, 1);
		}
	}

}

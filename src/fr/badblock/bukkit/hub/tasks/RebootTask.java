package fr.badblock.bukkit.hub.tasks;

import java.util.Random;

import org.bukkit.Bukkit;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class RebootTask extends CustomTask {

	private long	time  = -1;
	private long	boot  = System.currentTimeMillis();
	private long    max   = (new Random().nextInt(3600)) * 1000 + (36000 * 1);

	public RebootTask() {
		super(20, 20);
	}

	@Override
	public void done() {
		if (time == 0) {
			time = -1;
			// Broadcast what's going on
			GameAPI.i18n().broadcast("hub.reboot.teleport");
			// Teleport to the "lobby" skeleton server.
			BukkitUtils.forEachPlayers(player -> player.sendPlayer("lobby"));
			// We wait to shutdown the server.
			TaskManager.runTaskLater(new Runnable() {
					@Override
					public void run() {
						// Check if players are here
						int onlinePlayers = Bukkit.getOnlinePlayers().size();
						if (onlinePlayers > 0) {
							// Teleport them to the login skeleton server.
							BukkitUtils.forEachPlayers(player -> player.sendPlayer("lobby"));
							GameAPI.getAPI().setWhitelistStatus(true);
							// We set a delay to shutdown the server because of players who still here :(
							TaskManager.runTaskLater(new Runnable() {
								@Override
								public void run() {
									Bukkit.shutdown();
								}
							}, 1000);
						}else{
							Bukkit.shutdown();
						}
					}
				}, 200);
				return;
			}
			if (time != -1 && time > 0) time--;
			if (time == 900 || time == 600 || time == 300 || time == 120 || time == 60 || time == 30 || time == 15
					|| time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
				if (time <= 30) {
					// Don't accept connections anymore
					GameAPI.getAPI().setWhitelistStatus(true);
				}
				if (time > 60) {
					GameAPI.i18n().broadcast("hub.reboot.reboot_minutes", time / 60);
				}else if (time == 60) {
					GameAPI.i18n().broadcast("hub.reboot.reboot_minute");
				}else if (time > 1) {
					GameAPI.i18n().broadcast("hub.reboot.reboot_seconds", time);
				}else{
					GameAPI.i18n().broadcast("hub.reboot.reboot_second");
				}
				return;
			}
			if (System.currentTimeMillis() - boot >= max && time == -1) {
				time = 900;
			}
	}

}

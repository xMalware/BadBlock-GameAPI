package fr.badblock.game.core18R3.tasks;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class LagTask implements Runnable {

	private long 	time;
	
	public LagTask() {
		TaskManager.scheduleSyncRepeatingTask("lagTask", this, 20, 20);
		this.time = System.currentTimeMillis();
	}
	
	@Override
	public void run() {
		long time = System.currentTimeMillis();
		long difference = time - this.time;
		if (difference >= 5000) {
			double d = (difference - 1000);
			d /= 1000;
			new SlackMessage("SpigotLag : " + GameAPI.getServerName() + " | Lag de " + String.format("%.2f", d) + " secondes", "Monitoring - Spigot", "http://icon-icons.com/icons2/822/PNG/512/alert_icon-icons.com_66469.png", false).run();
		}
		this.time = time;
	}
	
}

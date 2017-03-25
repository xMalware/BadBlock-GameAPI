package fr.badblock.bukkit.hub.listeners;

import org.bukkit.event.EventHandler;
import org.github.paperspigot.exception.ServerSchedulerException;

public class ServerSchedulerListener extends _HubListener {

	@EventHandler
	public void onServerSchedulerException(ServerSchedulerException event) {
		event.printStackTrace();
	}
	
}

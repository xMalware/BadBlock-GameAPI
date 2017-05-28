package fr.badblock.bukkit.hub.listeners.entities;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import fr.badblock.bukkit.hub.listeners._HubListener;

public class CreatureSpawnListener extends _HubListener {

	@EventHandler (ignoreCancelled = false)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason().equals(SpawnReason.CUSTOM))
			return;
		event.setCancelled(true);
	}

}

package fr.badblock.bukkit.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkUnloadListener extends _HubListener {

	@EventHandler (ignoreCancelled = false)
	public void onChunkUnload(ChunkUnloadEvent event) {
		event.setCancelled(true);
	}

}

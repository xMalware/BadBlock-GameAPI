package fr.badblock.bukkit.hub.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

public class EntityExplodeListener extends _HubListener {

	@EventHandler (ignoreCancelled = false, priority = EventPriority.LOWEST)
	public void onEntityCombustByBlockEvent(EntityExplodeEvent event) {
		event.getEntity().getNearbyEntities(5, 5, 5).forEach(entity -> {
			entity.setVelocity(new Vector(entity.getVelocity().getX(), 3.0D, entity.getVelocity().getZ()));
		});
		/*Location loc = PlayerMoveListener.spawn;
		Location loca = event.getLocation();
		if (loca.distance(loc) <= 15) {
			SEntryInfosListener.tempNPCs.values().forEach(npc -> npc.npcs.forEach(npca -> npca.getBukkitEntity().setVelocity(new Vector(npca.getBukkitEntity().getVelocity().getX(), 3.0D, npca.getBukkitEntity().getVelocity().getZ()))));
		}*/
	}

}

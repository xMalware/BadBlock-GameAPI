package fr.badblock.game.v1_8_R3.listeners.fixs;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.badblock.gameapi.BadListener;

public class UselessDamageFixListener extends BadListener {
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onEntityDamageLOWEST(EntityDamageEvent e) {
		if(e.getCause().equals(DamageCause.FALL) && e.getEntityType() == EntityType.PLAYER) {
			if (e.getDamage() <= 1) 
				e.setCancelled(true);
		}
	}
}

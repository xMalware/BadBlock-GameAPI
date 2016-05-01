package fr.badblock.game.v1_8_R3.listeners.fixs;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.badblock.gameapi.BadListener;

public class ArrowBugFixListener extends BadListener {
	private UUID firstPlayer;

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
	public void onPlayerShootArrow(EntityShootBowEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		
		Player player = (Player) e.getEntity();
		
		if (player.getUniqueId().equals(firstPlayer)) {
			Arrow oldArrow = (Arrow) e.getProjectile();

			Arrow arrow = copyOf(oldArrow, pickable(e));
			e.setProjectile(arrow);
		}
	}
	
	protected Arrow copyOf(Arrow base, int pickable){
		Arrow arrow = base.getLocation().getWorld().spawn(base.getLocation(), Arrow.class);
		
		arrow.setShooter(base.getShooter());
		
		((CraftArrow) arrow).getHandle().fromPlayer = pickable;
		
		arrow.setCritical(base.isCritical());
		arrow.setVelocity(base.getVelocity());
		arrow.setFireTicks(base.getFireTicks());
		arrow.setKnockbackStrength(base.getKnockbackStrength());
	
		return arrow;
	}

	public int pickable(EntityShootBowEvent event) {
		if (event.getBow().containsEnchantment(Enchantment.ARROW_INFINITE) || ((Player)event.getEntity()).getGameMode().equals(GameMode.CREATIVE)) return 0;
		return 1;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST) 
	public void onPlayerJoin(PlayerJoinEvent e) {
		if(firstPlayer == null) 
			firstPlayer = e.getPlayer().getUniqueId();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST) 
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(e.getPlayer().getUniqueId().equals(firstPlayer)) 
			firstPlayer = null;
	}
}

package fr.badblock.game.core18R3.listeners.fixs;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.badblock.gameapi.BadListener;

public class BlockPlaceFixListener extends BadListener {
	@EventHandler(ignoreCancelled = false, priority=EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e)
	{
		if(e.getClickedBlock() == null || e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(e.getItem() == null || !e.getItem().getType().isBlock()) return;
		Block current = e.getClickedBlock().getRelative(e.getBlockFace());
		
		if(current.getType() != Material.AIR) return;

		for(Entity entity : current.getChunk().getEntities())
		{
			if(entity.getType() == EntityType.PLAYER)
			{
				Player p = (Player) entity;
				
				if(p.getEyeLocation().getBlock().equals(current) || p.getLocation().getBlock().equals(current))
				{
					e.setCancelled(true);
				}
			}
		}
	}
}

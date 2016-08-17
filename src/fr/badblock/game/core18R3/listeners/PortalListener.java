package fr.badblock.game.core18R3.listeners;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.portal.Portal;

public class PortalListener extends BadListener {
	@EventHandler(ignoreCancelled=true)
	public void onPortalCreate(EntityCreatePortalEvent e){
		boolean cancel = false;
		for(BlockState b : e.getBlocks()){
			Portal p = GameAPI.getAPI().getPortal(b.getBlock().getLocation());
			
			if(p != null){
				cancel = true; break;
			}
		}
		if(cancel) e.setCancelled(true);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Portal p = GameAPI.getAPI().getPortal(e.getPlayer().getLocation());
		
		if(p != null){
			e.getPlayer().teleport(e.getPlayer().getLocation().getWorld().getSpawnLocation());
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onWaterOrLavaFlow(BlockFromToEvent e){
		Portal p = GameAPI.getAPI().getPortal(e.getBlock().getLocation());

		if(p != null){
			Portal p2 = GameAPI.getAPI().getPortal(e.getToBlock().getLocation());
			
			if(!p.equals(p2)) e.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void blockPhysics(BlockPhysicsEvent e) {
		if((e.getChangedType() == Material.PORTAL) || (e.getBlock().getType() == Material.PORTAL)){
			Portal p = GameAPI.getAPI().getPortal(e.getBlock().getLocation());
			
			if(p != null)
				e.setCancelled(true);
		}
	}
}

package fr.badblock.game.core18R3.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;
import fr.badblock.gameapi.utils.selections.Vector3f;

public class PlayerSelectionListener extends BadListener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK){
			
			if(ItemStackUtils.isValid(e.getItem()) && e.getItem().getType() == Material.BLAZE_ROD){
				GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();
				
				if(!player.hasAdminMode()) return;
				
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
					player.setSecondVector(new Vector3f(e.getClickedBlock().getLocation()));
					player.sendTranslatedMessage("commands.selection-first", e.getClickedBlock().getX(), e.getClickedBlock().getY(), e.getClickedBlock().getZ());
				} else {
					player.setFirstVector(new Vector3f(e.getClickedBlock().getLocation()));
					player.sendTranslatedMessage("commands.selection-second", e.getClickedBlock().getX(), e.getClickedBlock().getY(), e.getClickedBlock().getZ());
				}
				
				e.setCancelled(true);
				
			}
		}
		
		if(GameAPI.getAPI().isCompassSelectNearestTarget() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
			if(ItemStackUtils.isValid(e.getItem()) && e.getMaterial() == Material.COMPASS){
				
				double minDistance   = 0.0d;
				Entity nearestEntity = null;
				
				for(Entity entity : e.getPlayer().getNearbyEntities(32.0d, 32.0d, 32.0d)){
					if(entity.getType() == EntityType.PLAYER){

						if(nearestEntity == null){
							nearestEntity = entity;
						} else if(entity.getLocation().distance(e.getPlayer().getLocation()) <= minDistance){
							nearestEntity = entity;
						}
						
					}
				}
				
				BadblockPlayer player = (BadblockPlayer) e.getPlayer();
				
				if(nearestEntity == null){
					player.sendTranslatedMessage("game.compass.notarget");					
				} else {
					player.sendTranslatedMessage("game.compass.target", nearestEntity.getName());
					player.setCompassTarget(nearestEntity.getLocation());
				}
				
			}
		}
	}
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent e){
		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();
		player.setFirstVector(null);
		player.setSecondVector(null);
	}
}

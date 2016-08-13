package fr.badblock.game.core18R3.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.BadListener;
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
	}
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent e){
		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();
		player.setFirstVector(null);
		player.setSecondVector(null);
	}
}

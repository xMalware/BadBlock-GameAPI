package fr.badblock.game.v1_8_R3.listeners.mapprotector;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;

public class PlayerMapProtectorListener extends BadListener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();
		
		if(GameAPI.getAPI().getMapProtector().healOnJoin(player)) {
			player.heal();
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		if(!GameAPI.getAPI().getMapProtector().blockPlace(player, e.getBlock()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		if(!GameAPI.getAPI().getMapProtector().blockBreak(player, e.getBlock()))
			e.setCancelled(true);
	}

	/* ItemFrames */

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		if(e.getCause() == DamageCause.ENTITY_ATTACK && e.getEntityType() == EntityType.ITEM_FRAME){
			if(e.getDamager().getType() == EntityType.PLAYER){
				BadblockPlayer player = (BadblockPlayer) e.getDamager();

				if(!GameAPI.getAPI().getMapProtector().modifyItemFrame(player, e.getEntity())){
					e.setCancelled(true);
				}
			} else {
				if(!GameAPI.getAPI().getMapProtector().modifyItemFrame(e.getEntity())){
					e.setCancelled(true);
				}
			}
		} else if(e.getEntityType() == EntityType.PLAYER){
			BadblockPlayer player = (BadblockPlayer) e.getEntity();

			if(!GameAPI.getAPI().getMapProtector().canBeingDamaged(player)){
				e.setCancelled(true);
			}
		} else {
			if(!GameAPI.getAPI().getMapProtector().canEntityBeingDamaged(e.getEntity())){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onHangingBreakEntity(HangingBreakByEntityEvent e) {
		if(e.getEntity().getType() == EntityType.ITEM_FRAME && e.getRemover().getType() == EntityType.PLAYER){
			BadblockPlayer player = (BadblockPlayer) e.getRemover();

			if(!GameAPI.getAPI().getMapProtector().modifyItemFrame(player, e.getEntity())){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onLostFood(FoodLevelChangeEvent e){
		if(e.getEntityType() == EntityType.PLAYER){
			if(e.getFoodLevel() != 20){
				if(!GameAPI.getAPI().getMapProtector().canLostFood((BadblockPlayer) e.getEntity())){
					e.setCancelled(true);
					e.setFoodLevel(20);
				}
			}
		}
	}

	@EventHandler
	public void onEnterBed(PlayerBedEnterEvent e){
		if(!GameAPI.getAPI().getMapProtector().canUseBed((BadblockPlayer) e.getPlayer(), e.getBed()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onUsePortal(PlayerPortalEvent e){
		if(!GameAPI.getAPI().getMapProtector().canUsePortal((BadblockPlayer) e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent e){
		if(!GameAPI.getAPI().getMapProtector().canDrop((BadblockPlayer) e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onDropItem(PlayerPickupItemEvent e){
		if(!GameAPI.getAPI().getMapProtector().canPickup((BadblockPlayer) e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onFillBucket(PlayerBucketFillEvent e){
		if(!GameAPI.getAPI().getMapProtector().canFillBucket((BadblockPlayer) e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onEmptyBucket(PlayerBucketEmptyEvent e){
		if(!GameAPI.getAPI().getMapProtector().canEmptyBucket((BadblockPlayer) e.getPlayer()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(!GameAPI.getAPI().getMapProtector().canInteract((BadblockPlayer) e.getPlayer(), e.getAction(), e.getClickedBlock()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent e){
		if(!GameAPI.getAPI().getMapProtector().canInteractEntity((BadblockPlayer) e.getPlayer(), e.getRightClicked()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onPrepareEnchantment(PrepareItemEnchantEvent e){
		if(!GameAPI.getAPI().getMapProtector().canEnchant((BadblockPlayer) e.getEnchanter(), e.getEnchantBlock())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onEnchant(EnchantItemEvent e){
		if(!GameAPI.getAPI().getMapProtector().canEnchant((BadblockPlayer) e.getEnchanter(), e.getEnchantBlock())){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e){
		if(e.getCause() == DamageCause.ENTITY_ATTACK) return;
		
		if(e.getEntityType() == EntityType.PLAYER){
			BadblockPlayer player = (BadblockPlayer) e.getEntity();

			if(!GameAPI.getAPI().getMapProtector().canBeingDamaged(player)){
				e.setCancelled(true);
			}
		} else if(e.getEntityType() != EntityType.ITEM_FRAME){
			if(!GameAPI.getAPI().getMapProtector().canEntityBeingDamaged(e.getEntity())){
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void soilChangePlayer(PlayerInteractEvent e) {
		if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL) {
			if(!GameAPI.getAPI().getMapProtector().canSoilChange(e.getClickedBlock()))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void soilChangeEntity(EntityInteractEvent e) {
		if(e.getEntityType() != EntityType.PLAYER && e.getBlock().getType() == Material.SOIL) {
			if(!GameAPI.getAPI().getMapProtector().canSoilChange(e.getBlock()))
				e.setCancelled(true);
		}
	}
}

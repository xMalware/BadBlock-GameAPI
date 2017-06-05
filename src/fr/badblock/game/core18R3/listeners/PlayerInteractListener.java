package fr.badblock.game.core18R3.listeners;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;
import fr.badblock.gameapi.utils.selections.Vector3f;

public class PlayerInteractListener extends BadListener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		doSelectionListener(e);
		doCompassListener(e);
	}
	
	private void doSelectionListener(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK){
			GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();
			if (player.getBadblockMode().equals(BadblockMode.SPECTATOR)) {
				if (!player.hasAdminMode()) e.setCancelled(true);
			}
			if(ItemStackUtils.isValid(player.getItemInHand()) && player.getItemInHand().getType().equals(Material.BLAZE_ROD)){
				
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
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			BadblockPlayer player = (BadblockPlayer) e.getPlayer();
			if (player.getInventory().getItemInHand() != null && player.getInventory().getItemInHand().getType() == Material.BOW) {
				long antiBowSpam = GamePlugin.getInstance().getAntiBowSpam();
				if (antiBowSpam == 0) return;
				String name = player.getName().toLowerCase();
				long time = BowSpamListener.shoot.containsKey(name) ? BowSpamListener.shoot.get(name) : 0;
				long current = System.currentTimeMillis();
				if (time > current) {
					e.setCancelled(true);
					double o = (time - current);
					double s = o / 1000.0D;
					String f = String.format("%.2f", s);
					BadblockPlayer badblockPlayer = (BadblockPlayer) player;
					long t = BowSpamListener.alert.containsKey(name) ? BowSpamListener.alert.get(name) : 0;
					if (t < current) {
						badblockPlayer.sendTranslatedMessage("game.bowspam", f);		
						BowSpamListener.alert.put(name, current + 500);
						player.sendTranslatedActionBar("game.bowspam", f);
					}else player.sendTranslatedActionBar("game.bowspam", f);
				}else player.sendTranslatedActionBar("game.youcanbow");
			}
		}
	}
	
	private void doCompassListener(PlayerInteractEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();
		
		if(player.hasAdminMode()){
			if(!ItemStackUtils.isValid(e.getItem()) || e.getMaterial() != Material.COMPASS){
				return;
			}
			
			e.setCancelled(true);
			
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
				Block blockTarget = null;
				boolean first 	  = true;
				boolean can   	  = false;
				
				int maxBlock = 40;
				int block	 = 0;
				
				Location location  = player.getEyeLocation().clone();
				Vector   direction = player.getEyeLocation().getDirection();
				
				Block 	   previous = location.getBlock();
				
				while(true){
					location = location.add(direction);
					Block b = location.getBlock();
					
					if(previous.equals(b)){
						continue;
					}
					
					previous = b;
					
					if(!b.getType().equals(Material.AIR)) {
						if(first){
							first = false;
						} else if(can){
							blockTarget = b;
							break;
						}
					} else if(!first){
						can = true;
					}
					
					block++;
					
					if(block > maxBlock)
						break;
				}
				
				if(blockTarget == null){
					player.sendTranslatedMessage("game.compass.tp.nothingpassthrough");
				} else {
					Location to = blockTarget.getLocation().add(0.5d, 1d, 0.5d);
					to.setYaw(e.getPlayer().getLocation().getYaw());
					to.setPitch(e.getPlayer().getLocation().getPitch());
					
					e.getPlayer().teleport(to);
				}
			} else if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK){
				Block blockTarget = null;
				
				for(Block b : player.getLineOfSight((HashSet<Material>) null, 200)) {
					if(!b.getType().equals(Material.AIR)) { 
						blockTarget = b; break; 
					}
				}
				
				if(blockTarget == null){
					player.sendTranslatedMessage("game.compass.tp.noblockinsight");
				} else {
					while(blockTarget.getRelative(BlockFace.UP).getType() != Material.AIR){
						blockTarget = blockTarget.getRelative(BlockFace.UP);
					}
					
					Location to = blockTarget.getLocation().add(0.5d, 1d, 0.5d);
					to.setYaw(e.getPlayer().getLocation().getYaw());
					to.setPitch(e.getPlayer().getLocation().getPitch());

					e.getPlayer().teleport(to);
				}

			}
		} else if(GameAPI.getAPI().isCompassSelectNearestTarget() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
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
				
				if(nearestEntity == null){
					player.sendTranslatedMessage("game.compass.target.notarget");					
				} else {
					player.sendTranslatedMessage("game.compass.target.target", nearestEntity.getName());
					player.setCompassTarget(nearestEntity.getLocation());
				}
				
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onInventoryInteract(InventoryInteractEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		GameBadblockPlayer player = (GameBadblockPlayer) event.getWhoClicked();
		if (!player.hasAdminMode() && (player.getBadblockMode().equals(BadblockMode.RESPAWNING) || player.getBadblockMode().equals(BadblockMode.SPECTATOR)))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent e){
		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();
		player.setFirstVector(null);
		player.setSecondVector(null);
	}
}

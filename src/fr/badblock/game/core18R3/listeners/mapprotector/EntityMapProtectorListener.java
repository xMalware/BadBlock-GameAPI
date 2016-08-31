package fr.badblock.game.core18R3.listeners.mapprotector;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.ProjectileHitBlockEvent;

public class EntityMapProtectorListener extends BadListener {
	@EventHandler
	public void onCombust(EntityCombustEvent e){
		if(!GameAPI.getAPI().getMapProtector().canCombust(e.getEntity()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e){
		if(!GameAPI.getAPI().getMapProtector().canSpawn(e.getEntity()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e){
		if(!GameAPI.getAPI().getMapProtector().canCreatureSpawn(e.getEntity(), e.getSpawnReason() == SpawnReason.CUSTOM))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent e){
		if(!GameAPI.getAPI().getMapProtector().canItemSpawn(e.getEntity()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent e){
		if(!GameAPI.getAPI().getMapProtector().canItemDespawn(e.getEntity()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent e){
		if(!GameAPI.getAPI().getMapProtector().allowExplosion(e.getBlock().getLocation())){
			e.blockList().clear();
		}
	}
	
	@EventHandler
	public void onBlockExplode(EntityExplodeEvent e){
		if(!GameAPI.getAPI().getMapProtector().allowExplosion(e.getEntity().getLocation())){
			e.blockList().clear();
		}
	}
	
	@EventHandler
	public void onArrowHitBlock(ProjectileHitBlockEvent e){
		if(e.getProjectile().getType() != EntityType.ARROW) return;
		
		if(GameAPI.getAPI().getMapProtector().destroyArrow()){
			e.getProjectile().remove();
		}
	}
}

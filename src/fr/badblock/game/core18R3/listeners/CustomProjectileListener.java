package fr.badblock.game.core18R3.listeners;

import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.badblock.game.core18R3.players.GameBadblockPlayer.ProjectileMetadata;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.ProjectileHitBlockEvent;

public class CustomProjectileListener extends BadListener {
	public static final String metadataKey = "customprojectileConsumer";
	
	@EventHandler(ignoreCancelled=true)
	public void onProjectileHitBlock(ProjectileHitBlockEvent e){
		Projectile projectile = (Projectile) e.getProjectile();
		
		if(projectile.hasMetadata(metadataKey)){
			ProjectileMetadata metadata = (ProjectileMetadata) projectile.getMetadata(metadataKey);
		
			metadata.value().accept(e.getBlock(), null);
			projectile.removeMetadata(metadataKey, GameAPI.getAPI());
		}
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onProjectileHitEntity(EntityDamageByEntityEvent e){
		if(!(e.getDamager() instanceof Projectile)){
			return;
		}
		
		Projectile projectile = (Projectile) e.getDamager();
		
		if(projectile.hasMetadata(metadataKey)){
			ProjectileMetadata metadata = (ProjectileMetadata) projectile.getMetadata(metadataKey);
		
			metadata.value().accept(null, e.getEntity());
			projectile.removeMetadata(metadataKey, GameAPI.getAPI());
		}
	}
}

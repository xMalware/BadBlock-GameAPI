package fr.badblock.game.v1_8_R3.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.fakedeaths.FakeDeathEvent;
import fr.badblock.gameapi.events.fakedeaths.FightingDeathEvent;
import fr.badblock.gameapi.events.fakedeaths.FightingDeathEvent.FightingDeaths;
import fr.badblock.gameapi.events.fakedeaths.NormalDeathEvent;
import fr.badblock.gameapi.events.fakedeaths.PlayerFakeRespawnEvent;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import lombok.NoArgsConstructor;

public class FakeDeathCaller extends BadListener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageEvent e){
		if(e.getCause() == DamageCause.ENTITY_ATTACK) return;

		if(!e.isCancelled() && e.getEntityType() == EntityType.PLAYER){
			BadblockPlayer player = (BadblockPlayer) e.getEntity();
			Entity		   killer = null;
			FakeDeathData  data	  = player.inGameData(FakeDeathData.class);
			FightingDeaths type   = data.lastPvPDamage;
			FakeDeathEvent event  = null;


			if(e.getDamage() >= player.getHealth()){
				e.setCancelled(true);

				if(data.lastDamager != -1){
					if(System.currentTimeMillis() - data.lastDamage < 30 * 1000L){
						killer = getEntity(player, data.lastDamager);
					}
				}

				if(killer != null){
					event = new FightingDeathEvent(player, killer, type, e.getCause());
				} else {
					event = new NormalDeathEvent(player, e.getCause());
				}

				Bukkit.getPluginManager().callEvent(event);

				if(!event.isCancelled()){
					death(player, event);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageByEntityEvent e){
		if(!e.isCancelled() && e.getEntityType() == EntityType.PLAYER){
			BadblockPlayer player = (BadblockPlayer) e.getEntity();
			Entity		   killer = getTrueEntity(e.getDamager());
			FakeDeathData  data	  = player.inGameData(FakeDeathData.class);
			FakeDeathEvent event  = null;

			FightingDeaths type   = FightingDeaths.INFIGHTING;

			if(e.getEntityType() == EntityType.ARROW)
				type = FightingDeaths.BOW;
			else if(e.getEntityType() == EntityType.SPLASH_POTION)
				type = FightingDeaths.POTION;

			if(e.getDamage() >= player.getHealth()){
				e.setCancelled(true);

				killer = e.getDamager();
				event = new FightingDeathEvent(player, killer, type, e.getCause());

				Bukkit.getPluginManager().callEvent(event);

				if(!event.isCancelled()){
					death(player, event);
				}			
			} else {
				data.lastDamage    = System.currentTimeMillis();
				data.lastPvPDamage = type;
				data.lastDamager   = killer.getEntityId();
			}
		}
	}

	private void death(BadblockPlayer p, FakeDeathEvent e){
		p.heal();
		p.feed();
		p.removePotionEffects();
		
		if(e.getDeathMessage() != null){
			e.getDeathMessage().broadcast();
		}
		
		if(e.isLightning()){
			p.getWorld().strikeLightning(p.getLocation());
		}
		
		if(e.getTimeBeforeRespawn() > 0){
			p.setBadblockMode(BadblockMode.RESPAWNING);
			
			new BukkitRunnable(){
				private int time = e.getTimeBeforeRespawn();
				
				@Override
				public void run(){
					if(!p.isValid() || !p.isOnline()){
						cancel(); return;
					}
					
					if(time == 0){
						cancel();
						
						respawn(p, e.getRespawnPlace());
					}
					
					p.sendTranslatedTitle(GameMessages.respawnTitleKey(), time);
					
					time--;
				}
			}.runTaskTimer(GameAPI.getAPI(), 0, 20L);
		} else {
			respawn(p, e.getRespawnPlace());
		}
	}

	private void respawn(BadblockPlayer player, Location location){
		if(location != null)
			player.teleport(location);
		else location = player.getLocation();
		
		Bukkit.getPluginManager().callEvent(new PlayerFakeRespawnEvent(player, location));
	}
	
	private Entity getTrueEntity(Entity base){
		if(base instanceof Projectile){
			Projectile projectile = (Projectile) base;

			if(projectile.getShooter() instanceof Entity)
				return (Entity) projectile.getShooter();
		}

		return base;
	}

	private Entity getEntity(Player player, int id){
		for(Entity entity : player.getWorld().getEntities()){
			if(entity.getEntityId() == id)
				return entity;
		}

		return null;
	}

	@NoArgsConstructor
	public static class FakeDeathData implements InGameData {
		long      	   lastDamage  		 = 0;
		FightingDeaths lastPvPDamage  	 = null;
		int			   lastDamager		 = -1;
	}
}

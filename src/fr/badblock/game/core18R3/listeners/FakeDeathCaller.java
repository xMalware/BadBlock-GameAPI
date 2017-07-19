package fr.badblock.game.core18R3.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.itemstack.GameItemExtra;
import fr.badblock.game.core18R3.itemstack.ItemStackExtras;
import fr.badblock.game.core18R3.players.ingamedata.CommandInGameData;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.api.PlayerReconnectionPropositionEvent;
import fr.badblock.gameapi.events.fakedeaths.FakeDeathEvent;
import fr.badblock.gameapi.events.fakedeaths.FightingDeathEvent;
import fr.badblock.gameapi.events.fakedeaths.FightingDeathEvent.FightingDeaths;
import fr.badblock.gameapi.events.fakedeaths.NormalDeathEvent;
import fr.badblock.gameapi.events.fakedeaths.PlayerFakeRespawnEvent;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import lombok.NoArgsConstructor;

public class FakeDeathCaller extends BadListener {
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	public void onDamage(EntityDamageEvent e){
		if(e.getCause() == DamageCause.ENTITY_ATTACK) return;

		if (e.getEntityType() == EntityType.PLAYER) {
			BadblockPlayer player = (BadblockPlayer) e.getEntity();
			Entity		   killer = null;
			FakeDeathData  data	  = player.inGameData(FakeDeathData.class);
			FightingDeaths type   = data.lastPvPDamage;
			FakeDeathEvent event  = null;

			if(player.inGameData(CommandInGameData.class).godmode){
				e.setCancelled(true); return;
			}

			if(e.getFinalDamage() >= player.getHealth()){
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

				event.getDrops().addAll(items(player.getInventory()));
				Bukkit.getPluginManager().callEvent(event);

				if(!event.isCancelled()){
					if(killer != null && doASKill(player, killer, !event.isKeepInventory())){
						iWillSurvive(player);
						return;
					}

					death(player, event);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled=true)
	public void onDamage(EntityDamageByEntityEvent e){
		if(e.getEntityType() == EntityType.PLAYER){
			BadblockPlayer player = (BadblockPlayer) e.getEntity();
			Entity		   killer = getTrueEntity(e.getDamager());
			FakeDeathData  data	  = player.inGameData(FakeDeathData.class);
			FakeDeathEvent event  = null;

			FightingDeaths type   = FightingDeaths.INFIGHTING;

			if(player.inGameData(CommandInGameData.class).godmode){
				e.setCancelled(true); return;
			}
			
			if(e.getEntityType() == EntityType.ARROW)
				type = FightingDeaths.BOW;
			else if(e.getEntityType() == EntityType.SPLASH_POTION)
				type = FightingDeaths.POTION;

			if(killer.getType() == EntityType.PLAYER){

				BadblockPlayer bkiller = (BadblockPlayer) killer;
				if(bkiller.inGameData(FakeDeathData.class).lastDamager == -1){

					if(player.inGameData(FakeDeathData.class).lastKill.containsKey(bkiller.getUniqueId()))
						player.inGameData(FakeDeathData.class).lastKill.remove(bkiller.getUniqueId());

				}

			}

			if(e.getFinalDamage() >= player.getHealth()){
				e.setCancelled(true);

				event = new FightingDeathEvent(player, killer, type, e.getCause());
				event.getDrops().addAll(items(player.getInventory()));

				Bukkit.getPluginManager().callEvent(event);

				if(!event.isCancelled()){
					if(doASKill(player, killer, !event.isKeepInventory())){
						iWillSurvive(player);
						return;
					}

					death(player, event);
				}
			} else {
				data.lastDamage    = System.currentTimeMillis();
				data.lastPvPDamage = type;
				data.lastDamager   = !killer.equals(player) ? killer.getEntityId() : -1;
			}
		}
	}

	private boolean doASKill(BadblockPlayer player, Entity killer, boolean drop){
		if(antiSpawnKill() && killer.getType() == EntityType.PLAYER){
			BadblockPlayer bKiller = (BadblockPlayer) killer;
			FakeDeathData  fData   = bKiller.inGameData(FakeDeathData.class);

			if(fData.alert < 0) fData.alert = 0;

			boolean changed = false;

			// Si un joueur a �t� tap� r�cemment
			if(fData.lastKill.containsKey(player.getUniqueId())){
				long delta = System.currentTimeMillis() - fData.lastKill.get(player.getUniqueId());

				// Il y a moins de 20 secondes
				if(delta < 20 * 1000){
					// On incr�mente
					fData.alert++;
					fData.lastSpawnkill = System.currentTimeMillis();
					changed = true;
				} else if(fData.alert > 0){
					fData.alert--;
				}
			}

			if(System.currentTimeMillis() - fData.lastSpawnkill > 120 * 1000)
				fData.alert = 0;

			fData.lastKill.put(player.getUniqueId(), System.currentTimeMillis());

			// Si il n'a pas tap� un joueur depuis 60 secondes, on l'enl�ve de la liste
			fData.lastKill.forEach((p, last) -> {
				long delta = System.currentTimeMillis() - fData.lastKill.get(player.getUniqueId());

				if(delta > 60 * 1000){ // 60 secondes
					fData.alert--;
					fData.lastKill.remove(p);
				}
			});

			if(fData.alert == 2 && changed){
				bKiller.sendTranslatedTitle("antispawnkill.warning.first");
				bKiller.sendTimings(20, 40, 20);
			}

			if(fData.alert == 3 && changed){
				bKiller.sendTranslatedTitle("antispawnkill.warning.second");
				bKiller.setHealth(1.0d);
			}

			if(fData.alert >= 10 && changed){
				cantReconnect.add(bKiller.getUniqueId());
				bKiller.kickPlayer(new TranslatableString("antispawnkill.kick").getAsLine(bKiller));

				if(drop)
					drop(items(bKiller.getInventory()), bKiller.getLocation());
			}

		}

		return false;
	}

	private List<UUID> cantReconnect = new ArrayList<>();

	@EventHandler
	public void onProposition(PlayerReconnectionPropositionEvent e){
		if(cantReconnect.contains(e.getPlayer()))
			e.setCancelled(true);
	}

	private boolean antiSpawnKill(){
		return GamePlugin.getInstance().isAntiSpawnKill();
	}

	private void iWillSurvive(BadblockPlayer p){
		p.heal();
		p.feed();
	}

	private List<ItemStack> items(PlayerInventory inventory){
		List<ItemStack> result = new ArrayList<>();

		for(ItemStack is : inventory.getContents())
			if(is != null && is.getType() != Material.AIR && can(is))
				result.add(is);
		for(ItemStack is : inventory.getArmorContents())
			if(is != null && is.getType() != Material.AIR && can(is))
				result.add(is);

		return result;
	}

	private boolean can(ItemStack item){
		GameItemExtra extra = ItemStackExtras.getExtra(item);
		return !(extra != null && !extra.isAllowDropOnDeath());
	}
	
	private void drop(List<ItemStack> items, Location place){
		for(ItemStack item : items){
			if(item != null && item.getType() != Material.AIR && can(item)){
				place.getWorld().dropItemNaturally(place, item);
			}
		}
	}

	private void death(BadblockPlayer p, FakeDeathEvent e){
		p.inGameData(CommandInGameData.class).lastLocation = p.getLocation();
		p.heal();
		p.feed();
		p.removePotionEffects();

		//p.playEffect(EntityEffect.DEATH);

		if(!e.isKeepInventory()){
			p.clearInventory();
			if (p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null) {
				p.getOpenInventory().getTopInventory().clear();
			}
			drop(e.getDrops(), p.getLocation());
		}

		p.inGameData(FakeDeathData.class).lastDamage    = 0;
		p.inGameData(FakeDeathData.class).lastPvPDamage = null;
		p.inGameData(FakeDeathData.class).lastDamager   = -1;

		if(e.getDeathMessage() != null){
			//System.out.println(e.getDeathMessage());
			Object end = e.getDeathMessageEnd() == null ? "" : e.getDeathMessageEnd();

			List<Object> res = new ArrayList<>();

			for(Object obj : e.getDeathMessage().getObjects())
				res.add(obj);
			res.add(end);
			//System.out.println(res);
			new TranslatableString(e.getDeathMessage().getKey(), res.toArray()).broadcast();

			for(Player player : Bukkit.getOnlinePlayers()){
				BadblockPlayer bplayer = (BadblockPlayer) player;
				bplayer.sendTranslatedActionBar(e.getDeathMessage().getKey(), res.toArray());
			}
		}

		if(e.isLightning()){
			p.getWorld().strikeLightningEffect(p.getLocation());
		}

		if(e.getTimeBeforeRespawn() > 0){
			p.setBadblockMode(BadblockMode.RESPAWNING);
			new BukkitRunnable(){
				private int time = e.getTimeBeforeRespawn();
				
				@Override
				public void run(){
					if(!p.isOnline()){
						cancel(); return;
					}

					if(time == 0){
						respawn(p, e.getRespawnPlace());
						cancel();
					} else {
						p.sendTranslatedTitle(GameMessages.respawnTitleKey(), time);
						time--;
					}
				}
			}.runTaskTimer(GameAPI.getAPI(), 4L, 20L);
		} else {
			respawn(p, e.getRespawnPlace());
		}
	}

	private void respawn(BadblockPlayer player, Location location){
		if(player.getBadblockMode()== BadblockMode.RESPAWNING)
			player.setBadblockMode(BadblockMode.PLAYER);

		player.clearTitle();
		
		if(location != null)
			player.teleport(location);
		else location = player.getLocation();

		Bukkit.getPluginManager().callEvent(new PlayerFakeRespawnEvent(player, location));
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				player.heal();
				player.feed();
			}
		}.runTaskLater(GameAPI.getAPI(), 1L);
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
		long      	    lastDamage  		 = 0;
		FightingDeaths  lastPvPDamage  	 = null;
		int			    lastDamager		 = -1;

		long			lastSpawnkill	 = 0;

		int			    alert			 = 0;
		int				kill			 = 0;
		Map<UUID, Long> lastKill		 = Maps.newConcurrentMap();
	}
}

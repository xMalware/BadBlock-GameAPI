package fr.badblock.game.core18R3.listeners.mapprotector;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;

public class BlockMapProtectorListener extends BadListener {
	@EventHandler
	public void onBlockDamage(BlockDamageEvent e){
		if(!GameAPI.getAPI().getMapProtector().canBlockDamage(e.getBlock()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onIgnite(BlockIgniteEvent e){
		if(!GameAPI.getAPI().getMapProtector().allowFire(e.getBlock()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onFade(BlockFadeEvent e){
		if(e.getBlock().getType() == Material.ICE || e.getBlock().getType() == Material.SNOW || e.getBlock().getType() == Material.SNOW_BLOCK)
			if(!GameAPI.getAPI().getMapProtector().allowMelting(e.getBlock()))
				e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockForm(BlockFormEvent e){
		if(!GameAPI.getAPI().getMapProtector().allowBlockFormChange(e.getBlock()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent e){
		if(!GameAPI.getAPI().getMapProtector().allowPistonMove(e.getBlock()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent e){
		if(!GameAPI.getAPI().getMapProtector().allowPistonMove(e.getBlock()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent e){
		if(!GameAPI.getAPI().getMapProtector().allowBlockPhysics(e.getBlock()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent e){
		if(!GameAPI.getAPI().getMapProtector().allowLeavesDecay(e.getBlock()))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onWeathChange(WeatherChangeEvent e){
		if(e.toWeatherState() && !GameAPI.getAPI().getMapProtector().allowRaining())
			e.setCancelled(true);
	}
}

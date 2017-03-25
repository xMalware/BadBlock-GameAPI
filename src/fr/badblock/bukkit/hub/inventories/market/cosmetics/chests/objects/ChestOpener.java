package fr.badblock.bukkit.hub.inventories.market.cosmetics.chests.objects;

import java.util.LinkedHashMap;

import org.bukkit.Location;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ChestOpener {

	public Location openerChestLocation;
	public LinkedHashMap<Location, Location> chestLocations;
	
}

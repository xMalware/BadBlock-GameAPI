package fr.badblock.bukkit.hub.inventories.market.properties;

import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.players.BadblockPlayer;

public abstract class CustomPropertyRunnable {
	
	public abstract void work(BadblockPlayer player, String ownItem);

	public abstract String getCustomI18n(BadblockPlayer player, String ownItem);
	
	public abstract ItemStack getItemStack(BadblockPlayer player, String ownItem);
	
}
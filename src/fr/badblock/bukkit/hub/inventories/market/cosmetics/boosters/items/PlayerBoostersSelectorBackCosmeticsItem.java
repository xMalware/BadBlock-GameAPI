
package fr.badblock.bukkit.hub.inventories.market.cosmetics.boosters.items;

import org.bukkit.Material;

import fr.badblock.bukkit.hub.inventories.abstracts.items.simples.CustomInventoryOpenerItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.boosters.inventories.BoosterInventory;

public class PlayerBoostersSelectorBackCosmeticsItem extends CustomInventoryOpenerItem {

	public PlayerBoostersSelectorBackCosmeticsItem() {
		super(BoosterInventory.class, "PlayerBoostersSelectorBackCosmeticsItem", Material.BARRIER);
	}

}

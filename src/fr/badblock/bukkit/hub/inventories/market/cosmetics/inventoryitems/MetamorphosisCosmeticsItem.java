
package fr.badblock.bukkit.hub.inventories.market.cosmetics.inventoryitems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.parent.inventories.parent.MetamorphosisInventory;
import fr.badblock.gameapi.players.BadblockPlayer;

public class MetamorphosisCosmeticsItem extends CustomItem {

	public MetamorphosisCosmeticsItem() {
		super("hub.items.metamorphosiscosmeticsitem", Material.PUMPKIN, "hub.items.metamorphosiscosmeticsitem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.closeInventory();
		/*if (!player.hasPermission("hub.soonbypass")) {
			player.sendTranslatedMessage("hub.items.functionsoon");
			return;
		}*/
		CustomInventory.get(MetamorphosisInventory.class).open(player);
	}

}

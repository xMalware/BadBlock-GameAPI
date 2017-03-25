
package fr.badblock.bukkit.hub.inventories.market.cosmetics.inventoryitems;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomPlayerInventory;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.boosters.inventories.PlayerBoostersSelectorInventory;
import fr.badblock.gameapi.players.BadblockPlayer;

public class BoosterCosmeticsItem extends CustomItem {

	public BoosterCosmeticsItem() {
		super("hub.items.boostercosmeticsitem", Material.EXP_BOTTLE, "hub.items.boostercosmeticsitem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.closeInventory();
		/*if (!player.hasPermission("hub.chestsaccess")) {
			player.sendTranslatedMessage("hub.items.functionsoon");
			return;
		}*/
		CustomPlayerInventory.get(PlayerBoostersSelectorInventory.class, player).open();
	}

}

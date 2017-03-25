package fr.badblock.bukkit.hub.inventories.join.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.inventoryitems.CosmeticsInventory;
import fr.badblock.gameapi.players.BadblockPlayer;

public class GadgetsPlayerItem extends CustomItem {

	public GadgetsPlayerItem() {
		// super("§bGadgets §7(clic droit)", Material.BLAZE_ROD, "", "§c>
		// §7Utilise des §bcosmétiques§7, des", "§beffets §7et bien plus
		// encore... !");
		super("hub.items.gadgetsplayeritem", Material.BLAZE_ROD, "hub.items.gadgetsplayeritem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK, ItemAction.RIGHT_CLICK_AIR,
				ItemAction.RIGHT_CLICK_BLOCK);
	}

	@Override
	public void onClick(BadblockPlayer lobbyPlayer, ItemAction itemAction, Block clickedBlock) {
		CustomInventory.get(CosmeticsInventory.class).open(lobbyPlayer);
	}

}

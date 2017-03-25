package fr.badblock.bukkit.hub.inventories.settings.settings.friends;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.gameapi.players.BadblockPlayer;

public class FriendsSettingsItem extends CustomItem {

	public FriendsSettingsItem() {
		// super("§bAutoriser les demandes d'amis", Material.PAPER);
		super("hub.items.friendsaddingsettingsitem", Material.SKULL_ITEM, (byte) 3,
				"hub.items.friendsaddingsettingsitem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		CustomInventory.get(FriendsSettingsInventory.class).open(player);
	}

}

package fr.badblock.bukkit.hub.inventories.join.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class ShopPlayerItem extends CustomItem {

	public ShopPlayerItem() {
		// super("§eBoutique §7(clic droit)", Material.GOLD_INGOT, "", "§c>
		// §7Achète des §bgrades§7, des", "§bobjets rares§7 pour découvrir
		// toutes", "§7les possibilités du serveur !");
		super("hub.items.shopplayeritem", Material.GOLD_INGOT, "hub.items.shopplayeritem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK, ItemAction.RIGHT_CLICK_AIR,
				ItemAction.RIGHT_CLICK_BLOCK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.closeInventory();
		if (!player.hasPermission("hub.soonbypass")) {
			player.sendTranslatedMessage("hub.items.functionsoon");
			return;
		}
		HubPlayer hubPlayer = HubPlayer.get(player);
		hubPlayer.getShopInventory().open(player);
	}

}

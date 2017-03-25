
package fr.badblock.bukkit.hub.inventories.shop.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.ConfigUtils;

public class OtherKitFreeBuildShopItem extends CustomItem {

	@SuppressWarnings("deprecation")
	public OtherKitFreeBuildShopItem() {
		super("hub.items.otherkitfreebuildshopitem.displayname", Material.getMaterial(91), "hub.items.otherkitfreebuildshopitem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		BadBlockHub instance = BadBlockHub.getInstance();
		int shopPoints = ConfigUtils.getInt(instance, "shoppoints." + this.getClass().getCanonicalName().toLowerCase().replace("shopitem", ""));
		if (shopPoints > player.getPlayerData().getShopPoints()) {
			player.sendTranslatedMessage("hub.shoppoints", shopPoints - player.getPlayerData().getShopPoints());
			return;
		}
		player.getPlayerData().removeShopPoints(shopPoints);
		instance.getShopLinkerAPI().sendShopdata("freebuild", player.getName(), "KitAutre");
		player.sendTranslatedMessage("hub.bought", player.getTranslatedMessage(this.getName())[0]);
	}

}

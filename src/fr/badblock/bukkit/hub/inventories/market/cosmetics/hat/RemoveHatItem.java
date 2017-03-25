
package fr.badblock.bukkit.hub.inventories.market.cosmetics.hat;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.gameapi.players.BadblockPlayer;

public class RemoveHatItem extends CustomItem {

	@SuppressWarnings("deprecation")
	public RemoveHatItem() {
		super("hub.items.RemoveMetamorphosisChoiceCosmeticsItem.name", Material.getMaterial(397), (byte) 3,
				"hub.items.RemoveMetamorphosisChoiceCosmeticsItem.lore");
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction action, Block clickedBlock) {
		if (!player.isDisguised()) {
			player.sendTranslatedMessage("hub.items.removedisguiseitem.youarenot");
			return;
		}
		player.undisguise();
		player.sendTranslatedMessage("hub.items.RemoveMetamorphosisChoiceCosmeticsItem.success");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

}

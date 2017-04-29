
package fr.badblock.bukkit.hub.inventories.selector.googleauth;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.general.Callback;

public class AuthRemoveSelectorItem extends CustomItem {

	@SuppressWarnings("deprecation")
	public AuthRemoveSelectorItem() {
		super("hub.items.authremoveitem", Material.getMaterial(402));
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.closeInventory();
		String playerName = player.getName().toLowerCase();
		AuthUtils.getAuthKey(playerName, new Callback<String>() {
			@Override
			public void done(String key, Throwable throwable) {
				// clé non générée, rien à supprimer
				if (key == null || key.isEmpty()) {
					player.sendTranslatedMessage("hub.auth.nokey");
					return;
				}
				player.sendTranslatedMessage("hub.auth.removeinstructions");
			}
		});
	}
	
}

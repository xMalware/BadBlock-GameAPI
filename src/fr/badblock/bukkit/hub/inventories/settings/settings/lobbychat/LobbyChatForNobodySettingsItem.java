package fr.badblock.bukkit.hub.inventories.settings.settings.lobbychat;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.objects.HubStoredPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class LobbyChatForNobodySettingsItem extends CustomItem {

	public LobbyChatForNobodySettingsItem() {
		super("hub.items.lobbychatfornobodysettingsitem", Material.STAINED_CLAY, (byte) 14,
				"hub.items.lobbychatfornobodysettingsitem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.closeInventory();
		HubStoredPlayer hubStoredPlayer = HubStoredPlayer.get(player);
		if (!hubStoredPlayer.isHubChat()) {
			player.sendTranslatedMessage("hub.lobbychat.disable.already");
			return;
		}
		hubStoredPlayer.setHubChat(false);
		player.sendTranslatedMessage("hub.lobbychat.disable.ok");
		player.saveGameData();
	}

}
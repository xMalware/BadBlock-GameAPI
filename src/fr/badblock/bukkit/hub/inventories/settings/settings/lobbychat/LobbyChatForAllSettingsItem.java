package fr.badblock.bukkit.hub.inventories.settings.settings.lobbychat;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.objects.HubStoredPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class LobbyChatForAllSettingsItem extends CustomItem {

	public LobbyChatForAllSettingsItem() {
		super("hub.items.lobbychatforallsettingsitem", Material.STAINED_CLAY, (byte) 5,
				"hub.items.lobbychatforallsettingsitem.lore");
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
		if (hubStoredPlayer.isHubChat()) {
			player.sendTranslatedMessage("hub.lobbychat.enable.already");
			return;
		}
		hubStoredPlayer.setHubChat(true);
		player.sendTranslatedMessage("hub.lobbychat.enable.ok");
		player.saveGameData();
	}

}
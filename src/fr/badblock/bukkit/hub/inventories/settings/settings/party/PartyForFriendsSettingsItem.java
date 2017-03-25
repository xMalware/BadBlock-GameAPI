package fr.badblock.bukkit.hub.inventories.settings.settings.party;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.utils.RabbitUtils;
import fr.badblock.gameapi.players.BadblockPlayer;

public class PartyForFriendsSettingsItem extends CustomItem {

	public PartyForFriendsSettingsItem() {
		super("hub.items.partyforfriendssettingsitem", Material.STAINED_CLAY, (byte) 4,
				"hub.items.partyforfriendssettingsitem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		RabbitUtils.forceCommand(player, "party toggle FRIENDS");
		player.closeInventory();
	}

}
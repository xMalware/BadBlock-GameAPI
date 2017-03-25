package fr.badblock.bukkit.hub.inventories.settings.settings.language;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.gameapi.players.BadblockPlayer;

public class LanguageSettingsItem extends CustomItem {

	public LanguageSettingsItem() {
		// super("§bInvitations de groupe", Material.PAPER);
		super("hub.items.languagesettingsitem", Material.BANNER, (byte) 15, "hub.items.languagesettingsitem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.closeInventory();
		player.sendTranslatedMessage("hub.items.functionsoon");
	}

}

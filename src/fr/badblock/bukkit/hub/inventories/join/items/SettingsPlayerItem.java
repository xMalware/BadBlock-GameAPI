package fr.badblock.bukkit.hub.inventories.join.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.inventories.settings.SettingsInventory;
import fr.badblock.gameapi.players.BadblockPlayer;

public class SettingsPlayerItem extends CustomItem {

	public SettingsPlayerItem() {
		// super("§cParamètres & Statistiques §7(clic droit)", Material.DIODE,
		// "", "§c> §bParamétrez §7plusieurs §bvariables§7,", "§7et regardez
		// §bvos statistiques §7!");
		super("hub.items.settingsplayeritem", Material.DIODE, "hub.items.settingsplayeritem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK, ItemAction.RIGHT_CLICK_AIR,
				ItemAction.RIGHT_CLICK_BLOCK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		CustomInventory.get(SettingsInventory.class).open(player);
	}

}

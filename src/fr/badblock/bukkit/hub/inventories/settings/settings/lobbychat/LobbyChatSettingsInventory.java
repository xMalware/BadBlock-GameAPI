package fr.badblock.bukkit.hub.inventories.settings.settings.lobbychat;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.items.CyanStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.BackSettingsSettingsItem;

public class LobbyChatSettingsInventory extends CustomInventory {

	public LobbyChatSettingsInventory() {
		super("hub.items.lobbychatsettingsinventory", 3);
		BlueStainedGlassPaneItem blueStainedGlassPaneItem = new BlueStainedGlassPaneItem();
		this.setItem(blueStainedGlassPaneItem, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25);
		this.setItem(10, new LobbyChatDescriptionSettingsItem());
		this.setItem(14, new LobbyChatForAllSettingsItem());
		this.setItem(16, new LobbyChatForNobodySettingsItem());
		this.setItem(26, new BackSettingsSettingsItem());
		this.setNoFilledItem(new CyanStainedGlassPaneItem());
	}

}

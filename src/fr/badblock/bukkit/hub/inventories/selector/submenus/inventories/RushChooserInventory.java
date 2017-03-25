package fr.badblock.bukkit.hub.inventories.selector.submenus.inventories;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.hubchanger.HubChangerBackItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.rush.Rush2v2NBSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.rush.Rush4v4NBSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.rush.Rush4x1SelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.rush.Rush4x4NBSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.rush.RushBookSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.rush.RushDescSelectorItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;

public class RushChooserInventory extends CustomInventory {

	public RushChooserInventory() {
		// super("§cParamètres/Statistiques", 5);
		super("hub.items.rushselectoritem.submenutitle", 5);
		this.setItem(new BlueStainedGlassPaneItem(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 37, 38, 39, 40, 41, 42, 43);
		this.setItem(13, new RushDescSelectorItem());
		this.setItem(21, new Rush2v2NBSelectorItem());
		this.setItem(22, new Rush4v4NBSelectorItem());
		this.setItem(23, new Rush4x4NBSelectorItem());
		this.setItem(31, new Rush4x1SelectorItem());
		this.setItem(36, new RushBookSelectorItem());
		this.setItem(44, new HubChangerBackItem());
		this.setNoFilledItem(new LightBlueStainedGlassPaneItem());
	}

}

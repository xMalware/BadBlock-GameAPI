package fr.badblock.bukkit.hub.inventories.selector.submenus.inventories;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.hubchanger.HubChangerBackItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.tower.Tower2v2SelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.tower.TowerBookSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.tower.TowerDescSelectorItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;

public class TowerChooserInventory extends CustomInventory {

	public TowerChooserInventory() {
		// super("§cParamètres/Statistiques", 5);
		super("hub.items.towerselectoritem.submenutitle", 4);
		this.setItem(new BlueStainedGlassPaneItem(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 28, 29, 30, 31, 32, 33, 34);
		this.setItem(13, new TowerDescSelectorItem());
		this.setItem(22, new Tower2v2SelectorItem());
		this.setItem(27, new TowerBookSelectorItem());
		this.setItem(35, new HubChangerBackItem());
		this.setNoFilledItem(new LightBlueStainedGlassPaneItem());
	}

}

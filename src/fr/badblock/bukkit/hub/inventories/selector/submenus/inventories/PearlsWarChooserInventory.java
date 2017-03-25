package fr.badblock.bukkit.hub.inventories.selector.submenus.inventories;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.hubchanger.HubChangerBackItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.pearlswar.PearlsWar16SelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.pearlswar.PearlsWarBookSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.pearlswar.PearlsWarDescSelectorItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;

public class PearlsWarChooserInventory extends CustomInventory {

	public PearlsWarChooserInventory() {
		// super("§cParamètres/Statistiques", 5);
		super("hub.items.pearlswarselectoritem.submenutitle", 4);
		this.setItem(new BlueStainedGlassPaneItem(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 28, 29, 30, 31, 32, 33,
				34);
		this.setItem(13, new PearlsWarDescSelectorItem());
		this.setItem(22, new PearlsWar16SelectorItem());
		this.setItem(27, new PearlsWarBookSelectorItem());
		this.setItem(35, new HubChangerBackItem());
		this.setNoFilledItem(new LightBlueStainedGlassPaneItem());
	}

}

package fr.badblock.bukkit.hub.inventories.selector.submenus.inventories;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.hubchanger.HubChangerBackItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.speeduhc.SpeedUHCBookSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.speeduhc.SpeedUHCDescSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.speeduhc.SpeedUHCTeamSelectorItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;

public class SpeedUHCChooserInventory extends CustomInventory {

	public SpeedUHCChooserInventory() {
		// super("§cParamètres/Statistiques", 5);
		super("hub.items.speeduhcselectoritem.submenutitle", 4);
		this.setItem(new BlueStainedGlassPaneItem(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 28, 29, 30, 31, 32, 33,
				34);
		this.setItem(13, new SpeedUHCDescSelectorItem());
		//this.setItem(21, new SpeedUHCSoloSelectorItem());
		this.setItem(22, new SpeedUHCTeamSelectorItem());
		this.setItem(27, new SpeedUHCBookSelectorItem());
		this.setItem(35, new HubChangerBackItem());
		this.setNoFilledItem(new LightBlueStainedGlassPaneItem());
	}

}

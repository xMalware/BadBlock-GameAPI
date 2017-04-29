package fr.badblock.bukkit.hub.inventories.selector.googleauth;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;

public class AuthSelectorInventory extends CustomInventory {

	public AuthSelectorInventory() {
		// super("§6Sélecteur", 6);
		super("hub.items.selectorinventory", 3);
		LightBlueStainedGlassPaneItem lightBlueStainedGlassPaneItem = new LightBlueStainedGlassPaneItem();
		BlueStainedGlassPaneItem blueStainedGlassPaneItem = new BlueStainedGlassPaneItem();
		for (int id = 0; id < this.getLines() * 9; id++)
			if ((id == 0 || id < 9 || id % 9 == 0 || id == 17 || id == 26 || id == 35 || id == 44 || id == 53
					|| id > (9 * (this.getLines() - 1)) - 1))
				this.setItem(blueStainedGlassPaneItem, id);
		this.setItem(12, new AuthGenerateSelectorItem());
		this.setItem(14, new AuthRemoveSelectorItem());
		this.setItem(26, new AuthSelectorQuitItem());
		this.setNoFilledItem(lightBlueStainedGlassPaneItem);
	}

}

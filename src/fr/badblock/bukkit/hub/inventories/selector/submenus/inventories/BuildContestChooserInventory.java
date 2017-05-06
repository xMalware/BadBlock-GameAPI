package fr.badblock.bukkit.hub.inventories.selector.submenus.inventories;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.hubchanger.HubChangerBackItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.buildcontest.BuildContest16SelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.buildcontest.BuildContestBookSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.buildcontest.BuildContestDescSelectorItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;

public class BuildContestChooserInventory extends CustomInventory {

	public BuildContestChooserInventory() {
		super("hub.items.buildcontestselectoritem.submenutitle", 4);
		this.setItem(new BlueStainedGlassPaneItem(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 28, 29, 30, 31, 32, 33, 34);
		this.setItem(13, new BuildContestDescSelectorItem());
		this.setItem(22, new BuildContest16SelectorItem());
		this.setItem(27, new BuildContestBookSelectorItem());
		this.setItem(35, new HubChangerBackItem());
		this.setNoFilledItem(new LightBlueStainedGlassPaneItem());
	}

}

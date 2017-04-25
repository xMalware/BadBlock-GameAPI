package fr.badblock.bukkit.hub.inventories.selector;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.selector.dev.DevSelectorInventoryOpenItem;
import fr.badblock.bukkit.hub.inventories.selector.items.BuildSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.CTSSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.DayZSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.FreeBuildSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.HubChangerSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.PearlsWarSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.PvPBoxSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.PvPFactionSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.QuitSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.RushSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.SkyBlockSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.SpaceBallsSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.SpawnSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.SpeedUHCSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.StaffRoomSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.SurvivalGamesSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.TowerSelectorItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;

public class SelectorInventory extends CustomInventory {

	public SelectorInventory() {
		// super("§6Sélecteur", 6);
		super("hub.items.selectorinventory", 6);
		LightBlueStainedGlassPaneItem lightBlueStainedGlassPaneItem = new LightBlueStainedGlassPaneItem();
		this.setItem(lightBlueStainedGlassPaneItem, 28, 29, 30, 31, 32, 33, 34);
		this.setItem(0, new SpawnSelectorItem());
		this.setItem(4, new StaffRoomSelectorItem());
		this.setItem(7, new DevSelectorInventoryOpenItem());
		this.setItem(8, new BuildSelectorItem());
		this.setItem(19, new TowerSelectorItem());
		this.setItem(20, new RushSelectorItem());
		this.setItem(21, new SpeedUHCSelectorItem());
		this.setItem(22, new CTSSelectorItem());
		this.setItem(23, new SurvivalGamesSelectorItem());
		this.setItem(24, new SpaceBallsSelectorItem());
		this.setItem(25, new PearlsWarSelectorItem());
		this.setItem(38, new PvPFactionSelectorItem());
		this.setItem(39, new PvPBoxSelectorItem());
		this.setItem(40, new SkyBlockSelectorItem());
		this.setItem(41, new FreeBuildSelectorItem());
		this.setItem(42, new DayZSelectorItem());
		this.setItem(45, new HubChangerSelectorItem());
		this.setItem(53, new QuitSelectorItem());
		this.setItem(53, new QuitSelectorItem());
	}

}

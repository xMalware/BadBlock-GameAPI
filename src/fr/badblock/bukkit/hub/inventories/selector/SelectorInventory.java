package fr.badblock.bukkit.hub.inventories.selector;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.selector.items.BuildContestSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.CTSSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.DayZSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.FreeBuildSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.GoogleAuthSelectorItem;
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
import fr.badblock.bukkit.hub.inventories.selector.items.SurvivalGamesSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.TowerSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.VIPZoneSelectorItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;

public class SelectorInventory extends CustomInventory {

	public SelectorInventory() {
		super("hub.items.selectorinventory", 6);
		LightBlueStainedGlassPaneItem lightBlueStainedGlassPaneItem = new LightBlueStainedGlassPaneItem();
		this.setItem(lightBlueStainedGlassPaneItem, 19, 20, 21, 13, 23, 24, 25, 27, 35, 37, 43, 47, 48, 49, 50, 51);
		this.setItem(0, new SpawnSelectorItem());
		this.setItem(4, new GoogleAuthSelectorItem());
		this.setItem(8, new VIPZoneSelectorItem());
		this.setItem(22, new BuildContestSelectorItem());
		this.setItem(28, new TowerSelectorItem());
		this.setItem(29, new RushSelectorItem());
		this.setItem(30, new SpeedUHCSelectorItem());
		this.setItem(31, new CTSSelectorItem());
		this.setItem(32, new SurvivalGamesSelectorItem());
		this.setItem(33, new SpaceBallsSelectorItem());
		this.setItem(34, new PearlsWarSelectorItem());
		this.setItem(38, new PvPFactionSelectorItem());
		this.setItem(39, new PvPBoxSelectorItem());
		this.setItem(40, new SkyBlockSelectorItem());
		this.setItem(41, new FreeBuildSelectorItem());
		this.setItem(42, new DayZSelectorItem());
		this.setItem(45, new HubChangerSelectorItem());
		this.setItem(53, new QuitSelectorItem());
		this.setNoFilledItem(new BlueStainedGlassPaneItem());
	}

}

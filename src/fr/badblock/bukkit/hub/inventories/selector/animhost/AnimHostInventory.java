package fr.badblock.bukkit.hub.inventories.selector.animhost;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.selector.animhost.items.CTS8v8SelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.animhost.items.PearlsWar16SelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.animhost.items.Rush2v2NBSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.animhost.items.SpaceBalls4v4SelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.animhost.items.SpeedUHCSoloSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.animhost.items.SurvivalGames24SelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.animhost.items.Tower2v2SelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.buildcontest.BuildContest16SelectorItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;

public class AnimHostInventory extends CustomInventory {

	public AnimHostInventory() {
		super("hub.items.animhostinventory", 4);
		LightBlueStainedGlassPaneItem lightBlueStainedGlassPaneItem = new LightBlueStainedGlassPaneItem();
		this.setItem(lightBlueStainedGlassPaneItem, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35);
		this.addItem(new BuildContest16SelectorItem());
		this.addItem(new CTS8v8SelectorItem());
		this.addItem(new PearlsWar16SelectorItem());
		this.addItem(new Rush2v2NBSelectorItem());
		//this.addItem(new Rush2v2SelectorItem());
		//this.addItem(new Rush4v4NBSelectorItem());
		//this.addItem(new Rush4v4SelectorItem());
		//this.addItem(new Rush4x4NBSelectorItem());
		//this.addItem(new Rush4x4SelectorItem());
		this.addItem(new SpaceBalls4v4SelectorItem());
		this.addItem(new SpeedUHCSoloSelectorItem());
		//this.addItem(new SpeedUHCTeamSelectorItem());
		//this.addItem(new SurvivalGames24NoTeamSelectorItem());
		this.addItem(new SurvivalGames24SelectorItem());
		//this.addItem(new Tower2v2NBSelectorItem());
		this.addItem(new Tower2v2SelectorItem());
		//this.addItem(new Tower4v4NBSelectorItem());
		//this.addItem(new Tower4v4SelectorItem());
		//this.addItem(new Tower8v8NBSelectorItem());
		//this.addItem(new Tower8v8SelectorItem());
		this.setAsLastItem(new AnimHostQuitItem());
		this.setNoFilledItem(new BlueStainedGlassPaneItem());
	}

}

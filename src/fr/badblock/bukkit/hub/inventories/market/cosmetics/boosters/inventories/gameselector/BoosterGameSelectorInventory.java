package fr.badblock.bukkit.hub.inventories.market.cosmetics.boosters.inventories.gameselector;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.items.CyanStainedGlassPaneItem;

public class BoosterGameSelectorInventory extends CustomInventory {

	public BoosterGameSelectorInventory() {
		super("hub.items.booster.gameselectorinventory", 3);
		BlueStainedGlassPaneItem blueStainedGlassPaneItem = new BlueStainedGlassPaneItem();
		this.setItem(blueStainedGlassPaneItem, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25);
		this.setItem(10, new TowerBoosterItem());
		this.setItem(11, new RushBoosterItem());
		this.setItem(12, new SpeedUHCBoosterItem());
		this.setItem(13, new CTSBoosterItem());
		this.setItem(14, new SurvivalGamesBoosterItem());
		this.setItem(15, new SpaceBallsBoosterItem());
		this.setItem(16, new PearlsWarBoosterItem());
		this.setItem(26, new BackBoosterManagerItem());
		this.setNoFilledItem(new CyanStainedGlassPaneItem());
	}

}

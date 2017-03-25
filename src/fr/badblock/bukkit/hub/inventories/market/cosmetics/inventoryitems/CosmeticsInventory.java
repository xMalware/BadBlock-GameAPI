package fr.badblock.bukkit.hub.inventories.market.cosmetics.inventoryitems;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.items.CyanStainedGlassPaneItem;

public class CosmeticsInventory extends CustomInventory {

	public CosmeticsInventory() {
		super("hub.items.cosmeticsinventory", 6);
		BlueStainedGlassPaneItem blueStainedGlassPaneItem = new BlueStainedGlassPaneItem();
		this.setItem(blueStainedGlassPaneItem, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48,
				49, 50, 51, 52);
		this.setItem(22, new BoosterCosmeticsItem());
		this.setItem(29, new EffectsCosmeticsItem());
		this.setItem(30, new MountsCosmeticsItem());
		this.setItem(32, new MetamorphosisCosmeticsItem());
		this.setItem(33, new GadgetsCosmeticsItem());
		this.setItem(53, new QuitCosmeticsItem());
		this.setNoFilledItem(new CyanStainedGlassPaneItem());
	}

}

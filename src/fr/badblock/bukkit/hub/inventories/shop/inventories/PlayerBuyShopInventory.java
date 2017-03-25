package fr.badblock.bukkit.hub.inventories.shop.inventories;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.settings.items.SkullSettingsItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.shop.QuitShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.FactionGameShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.FreeBuildGameShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.GrayStainedGlassPaneShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.IslanderSkyBlockShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.MiniGamesShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.PvPBoxGameShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.RedStainedGlassPaneShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.SkyBlockGameShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.SkylanderSkyBlockShopItem;

public class PlayerBuyShopInventory extends CustomInventory {

	public PlayerBuyShopInventory() {
		super("hub.items.skyblockshopinventory", 1);
		LightBlueStainedGlassPaneItem blueStainedGlassPaneItem = new LightBlueStainedGlassPaneItem();
		this.setItem(blueStainedGlassPaneItem, 0, 8, 9, 17, 18, 26, 27, 35, 36);
		this.setItem(1, new SkullSettingsItem());
		this.setItem(2, new MiniGamesShopItem());
		this.setItem(3, new FactionGameShopItem());
		this.setItem(4, new SkyBlockGameShopItem());
		this.setItem(5, new FreeBuildGameShopItem());
		this.setItem(6, new PvPBoxGameShopItem());
		this.setItem(10, new GrayStainedGlassPaneShopItem());
		this.setItem(11, new GrayStainedGlassPaneShopItem());
		this.setItem(12, new GrayStainedGlassPaneShopItem());
		this.setItem(13, new RedStainedGlassPaneShopItem());
		this.setItem(14, new GrayStainedGlassPaneShopItem());
		this.setItem(15, new GrayStainedGlassPaneShopItem());
		this.setItem(16, new GrayStainedGlassPaneShopItem());
		this.setItem(19, new IslanderSkyBlockShopItem());
		this.setItem(20, new SkylanderSkyBlockShopItem());
		this.setAsLastItem(new QuitShopItem());
	}

}

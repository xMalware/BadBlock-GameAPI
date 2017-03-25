package fr.badblock.bukkit.hub.inventories.shop.inventories;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.settings.items.SkullSettingsItem;
import fr.badblock.bukkit.hub.inventories.settings.settings.LightBlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.shop.QuitShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.ArchitectFreeBuildShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.BlocksKitFreeBuildShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.BuilderFreeBuildShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.DecorationKitFreeBuildShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.FactionGameShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.FreeBuildGameShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.GrayStainedGlassPaneShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.MiniGamesShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.OtherKitFreeBuildShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.PvPBoxGameShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.RedStainedGlassPaneShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.RedstoneKitFreeBuildShopItem;
import fr.badblock.bukkit.hub.inventories.shop.items.SkyBlockGameShopItem;

public class FreeBuildShopInventory extends CustomInventory {

	public FreeBuildShopInventory() {
		super("hub.items.freebuildshopinventory", 5);
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
		this.setItem(13, new GrayStainedGlassPaneShopItem());
		this.setItem(14, new RedStainedGlassPaneShopItem());
		this.setItem(15, new GrayStainedGlassPaneShopItem());
		this.setItem(16, new GrayStainedGlassPaneShopItem());
		this.setItem(19, new BuilderFreeBuildShopItem());
		this.setItem(20, new ArchitectFreeBuildShopItem());
		this.setItem(28, new DecorationKitFreeBuildShopItem());
		this.setItem(29, new RedstoneKitFreeBuildShopItem());
		this.setItem(30, new OtherKitFreeBuildShopItem());
		this.setItem(31, new BlocksKitFreeBuildShopItem());
		this.setAsLastItem(new QuitShopItem());
	}

}

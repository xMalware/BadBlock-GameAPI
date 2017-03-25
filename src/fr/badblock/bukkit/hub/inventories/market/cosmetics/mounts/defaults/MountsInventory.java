package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.inventoryitems.BackCosmeticsItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.BatMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.BlazeMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.BunnyMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.CaveSpiderMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.ChickenMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.CowMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.CreeperMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.EndermanMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.GuardianMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.IronGolemMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.MagmaCubeMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.MushroomCowMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.OcelotMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.PegasusMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.PigMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.PigZombieMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.SheepMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.SilverfishMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.SkeletonMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.SlimeMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.SpiderMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.SquidMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.VillagerMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.WitchMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.WolfMountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.ZombieMountItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;

public class MountsInventory extends CustomInventory {

	public MountsInventory() {
		super("hub.items.mountsinventory", 5);
		BlueStainedGlassPaneItem blueStainedGlassPaneItem = new BlueStainedGlassPaneItem();
		this.setItem(blueStainedGlassPaneItem, 0, 1, 2, 3, 4, 5, 6, 7, 8, 37, 38, 39, 40, 41, 42, 43);
		this.addItem(new BatMountItem());
		this.addItem(new BlazeMountItem());
		this.addItem(new CaveSpiderMountItem());
		this.addItem(new ChickenMountItem());
		this.addItem(new CowMountItem());
		this.addItem(new CreeperMountItem());
		this.addItem(new EndermanMountItem());
		// this.addItem(new GhastMountItem());
		// this.addItem(new GiantMountItem());
		this.addItem(new GuardianMountItem());
		this.addItem(new PegasusMountItem());
		this.addItem(new IronGolemMountItem());
		this.addItem(new MagmaCubeMountItem());
		this.addItem(new MushroomCowMountItem());
		this.addItem(new OcelotMountItem());
		this.addItem(new PigMountItem());
		this.addItem(new PigZombieMountItem());
		this.addItem(new BunnyMountItem());
		this.addItem(new SheepMountItem());
		this.addItem(new SilverfishMountItem());
		this.addItem(new SkeletonMountItem());
		this.addItem(new SlimeMountItem());
		// this.addItem(new SnowmanMountItem());
		this.addItem(new SpiderMountItem());
		this.addItem(new SquidMountItem());
		this.addItem(new VillagerMountItem());
		this.addItem(new WitchMountItem());
		this.addItem(new WolfMountItem());
		this.addItem(new ZombieMountItem());
		this.setItem(36, new RemoveMountCosmeticsItem());
		this.setAsLastItem(new BackCosmeticsItem());
		// this.setNoFilledItem(new CyanStainedGlassPaneItem());
	}

}

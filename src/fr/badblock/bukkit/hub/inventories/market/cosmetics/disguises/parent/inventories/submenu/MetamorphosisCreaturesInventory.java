package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.parent.inventories.submenu;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.parent.executors.RemoveMetamorphosisChoiceCosmeticsItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.BatCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.BlazeCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.CaveSpiderCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.ChickenCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.CowCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.CreeperCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.EndermanCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.GhastCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.GuardianCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.HorseCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.MagmaCubeCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.MushroomCowCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.OcelotCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.PigCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.PigZombieCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.RabbitCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.SheepCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.SkeletonCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.SlimeCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.SnowmanCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.SpiderCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.SquidCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.VillagerCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.WitchCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.WitherSkullCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.WolfCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures.ZombieCreatureMetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.inventoryitems.BackCosmeticsItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;

public class MetamorphosisCreaturesInventory extends CustomInventory {

	public MetamorphosisCreaturesInventory() {
		super("hub.items.disguisescreaturesinventory", 5);
		BlueStainedGlassPaneItem blueStainedGlassPaneItem = new BlueStainedGlassPaneItem();
		this.setItem(blueStainedGlassPaneItem, 0, 1, 2, 3, 4, 5, 6, 7, 8, 37, 38, 39, 40, 41, 42, 43);
		this.addItem(new BatCreatureMetamorphosisItem());
		this.addItem(new BlazeCreatureMetamorphosisItem());
		this.addItem(new CaveSpiderCreatureMetamorphosisItem());
		this.addItem(new ChickenCreatureMetamorphosisItem());
		this.addItem(new CowCreatureMetamorphosisItem());
		this.addItem(new CreeperCreatureMetamorphosisItem());
		this.addItem(new EndermanCreatureMetamorphosisItem());
		this.addItem(new GhastCreatureMetamorphosisItem());
		// this.addItem(new GiantCreatureMetamorphosisItem());
		this.addItem(new GuardianCreatureMetamorphosisItem());
		this.addItem(new HorseCreatureMetamorphosisItem());
		// this.addItem(new IronGolemCreatureMetamorphosisItem());
		this.addItem(new MagmaCubeCreatureMetamorphosisItem());
		this.addItem(new MushroomCowCreatureMetamorphosisItem());
		this.addItem(new OcelotCreatureMetamorphosisItem());
		this.addItem(new PigCreatureMetamorphosisItem());
		this.addItem(new PigZombieCreatureMetamorphosisItem());
		this.addItem(new RabbitCreatureMetamorphosisItem());
		this.addItem(new SheepCreatureMetamorphosisItem());
		// this.addItem(new SilverfishCreatureMetamorphosisItem());
		this.addItem(new SkeletonCreatureMetamorphosisItem());
		this.addItem(new SlimeCreatureMetamorphosisItem());
		this.addItem(new SnowmanCreatureMetamorphosisItem());
		this.addItem(new SpiderCreatureMetamorphosisItem());
		this.addItem(new SquidCreatureMetamorphosisItem());
		this.addItem(new VillagerCreatureMetamorphosisItem());
		this.addItem(new WitchCreatureMetamorphosisItem());
		this.addItem(new WitherSkullCreatureMetamorphosisItem());
		this.addItem(new WolfCreatureMetamorphosisItem());
		this.addItem(new ZombieCreatureMetamorphosisItem());
		this.setItem(36, new RemoveMetamorphosisChoiceCosmeticsItem());
		this.setAsLastItem(new BackCosmeticsItem());
		// this.setNoFilledItem(new CyanStainedGlassPaneItem());
	}

}

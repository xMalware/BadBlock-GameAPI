package fr.badblock.bukkit.hub.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.selector.submenus.inventories.CTSChooserInventory;
import fr.badblock.bukkit.hub.inventories.selector.submenus.inventories.PearlsWarChooserInventory;
import fr.badblock.bukkit.hub.inventories.selector.submenus.inventories.RushChooserInventory;
import fr.badblock.bukkit.hub.inventories.selector.submenus.inventories.SpaceBallsChooserInventory;
import fr.badblock.bukkit.hub.inventories.selector.submenus.inventories.SpeedUHCChooserInventory;
import fr.badblock.bukkit.hub.inventories.selector.submenus.inventories.SurvivalGamesChooserInventory;
import fr.badblock.bukkit.hub.inventories.selector.submenus.inventories.TowerChooserInventory;
import fr.badblock.bukkit.hub.utils.MountManager;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.watchers.WatcherBlaze;
import fr.badblock.gameapi.packets.watchers.WatcherCreeper;
import fr.badblock.gameapi.packets.watchers.WatcherEnderman;
import fr.badblock.gameapi.packets.watchers.WatcherLivingEntity;
import fr.badblock.gameapi.packets.watchers.WatcherSheep;
import fr.badblock.gameapi.packets.watchers.WatcherSkeleton;
import fr.badblock.gameapi.packets.watchers.WatcherVillager;
import fr.badblock.gameapi.packets.watchers.WatcherZombie;
import fr.badblock.gameapi.utils.ConfigUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import lombok.Getter;

public class LinkedInventoryEntity {

	@Getter
	private static Map<Location, CustomInventory> data = new HashMap<>();
	@Getter
	private static List<FakeEntity<?>> fakeEntities = new ArrayList<>();

	public static void assign(Location location, CustomInventory customInventory) {
		data.put(location, customInventory);
	}

	public static void createAndAssign(Location location, EntityType entityType,
			Class<? extends WatcherLivingEntity> watcherEntity, String displayNameKey,
			Class<? extends CustomInventory> customInventory) {
		fakeEntities.add(MountManager.spawn(location, entityType, watcherEntity, false, false, false, false,
				new TranslatableString(displayNameKey)));
		assign(location, CustomInventory.get(customInventory));
	}

	public static void createAndAssign(String node, EntityType entityType,
			Class<? extends WatcherLivingEntity> watcherEntity, String displayNameKey,
			Class<? extends CustomInventory> customInventory) {
		createAndAssign(ConfigUtils.getLocation(BadBlockHub.getInstance(), node), entityType, watcherEntity,
				displayNameKey, customInventory);
	}

	public static void load() {
		createAndAssign("gamepnj.tower", EntityType.ZOMBIE, WatcherZombie.class, "hub.gamepnj.tower", TowerChooserInventory.class);
		createAndAssign("gamepnj.rush", EntityType.VILLAGER, WatcherVillager.class, "hub.gamepnj.rush", RushChooserInventory.class);
		createAndAssign("gamepnj.pearlswar", EntityType.ENDERMAN, WatcherEnderman.class, "hub.gamepnj.pearlswar", PearlsWarChooserInventory.class);
		createAndAssign("gamepnj.spaceballs", EntityType.CREEPER, WatcherCreeper.class, "hub.gamepnj.spaceballs", SpaceBallsChooserInventory.class);
		createAndAssign("gamepnj.speeduhc", EntityType.BLAZE, WatcherBlaze.class, "hub.gamepnj.speeduhc", SpeedUHCChooserInventory.class);
		createAndAssign("gamepnj.survivalgames", EntityType.SKELETON, WatcherSkeleton.class, "hub.gamepnj.survivalgames", SurvivalGamesChooserInventory.class);
		createAndAssign("gamepnj.cts", EntityType.SHEEP, WatcherSheep.class, "hub.gamepnj.cts", CTSChooserInventory.class);
	}

}

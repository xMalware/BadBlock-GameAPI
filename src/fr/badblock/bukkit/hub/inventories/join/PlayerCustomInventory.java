package fr.badblock.bukkit.hub.inventories.join;

import org.bukkit.inventory.PlayerInventory;

import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.inventories.join.items.ChestPlayerItem;
import fr.badblock.bukkit.hub.inventories.join.items.GadgetsPlayerItem;
import fr.badblock.bukkit.hub.inventories.join.items.GameSelectorPlayerItem;
import fr.badblock.bukkit.hub.inventories.join.items.HiderPlayerItem;
import fr.badblock.bukkit.hub.inventories.join.items.HostPlayerItem;
import fr.badblock.bukkit.hub.inventories.join.items.SettingsPlayerItem;
import fr.badblock.bukkit.hub.inventories.join.items.ShopPlayerItem;
import fr.badblock.bukkit.hub.inventories.selector.dev.DevSelectorInventoryOpenItem;
import fr.badblock.bukkit.hub.inventories.selector.items.BuildSelectorItem;
import fr.badblock.bukkit.hub.inventories.selector.items.StaffRoomSelectorItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum PlayerCustomInventory {

	GADGETS(0, new GadgetsPlayerItem(), null),
	SHOP(1, new ShopPlayerItem(), null),
	CHEST(3, new ChestPlayerItem(), "hub.openchest"),
	SELECTOR(4, new GameSelectorPlayerItem(), null),
	HOST(5, new HostPlayerItem(), null),
	HIDER(7, new HiderPlayerItem(), null),
	SETTINGS(8, new SettingsPlayerItem(), null),
	STAFFROOM(9, new StaffRoomSelectorItem(), "hub.staffroom"),
	_BLUE_10(10, new BlueStainedGlassPaneItem(), "hub.staffroom"),
	_BLUE_11(11, new BlueStainedGlassPaneItem(), "hub.staffroom"),
	_BLUE_12(12, new BlueStainedGlassPaneItem(), "hub.staffroom"),
	DEVSELECTOR(13, new DevSelectorInventoryOpenItem(), "hub.staffroom"),
	_BLUE_14(14, new BlueStainedGlassPaneItem(), "hub.staffroom"),
	_BLUE_15(15, new BlueStainedGlassPaneItem(), "hub.staffroom"),
	_BLUE_16(16, new BlueStainedGlassPaneItem(), "hub.staffroom"),
	BUILD(17, new BuildSelectorItem(), "hub.staffroom");
	
	public static void give(BadblockPlayer player) {
		player.clearInventory();
		PlayerInventory inventory = player.getInventory();
		inventory.setHeldItemSlot(4);
		//HubStoredPlayer hubStoredPlayer = HubStoredPlayer.get(player);
		for (PlayerCustomInventory item : values()) {
			if (item.getPermission() != null && !player.hasPermission(item.getPermission())) continue;
			//if (item.name().equalsIgnoreCase("HIDER") && hubStoredPlayer.hidePlayers) inventory.setItem(item.getSlot(), new HiderDisablePlayerItem().toItemStack(player));
			//else 
			inventory.setItem(item.getSlot(), item.getCustomItem().getStaticItem().get(player.getPlayerData().getLocale()));
		}
	}

	public static void load() {

	}

	@Setter
	private CustomItem customItem;

	@Getter@Setter
	private String permission;

	@Setter
	private int slot;

	PlayerCustomInventory(int slot, CustomItem customItem, String permission) {
		this.setSlot(slot);
		this.setCustomItem(customItem);
		this.setPermission(permission);
	}

}

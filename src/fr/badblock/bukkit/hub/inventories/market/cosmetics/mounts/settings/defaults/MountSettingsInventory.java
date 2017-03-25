package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomPlayerInventory;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.BackMountsCosmeticsItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.MountFunModeItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.MountPegasusModeItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.MountReverseModeItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.MountSizeItem;
import fr.badblock.bukkit.hub.inventories.settings.items.BlueStainedGlassPaneItem;
import fr.badblock.bukkit.hub.inventories.settings.items.CyanStainedGlassPaneItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class MountSettingsInventory extends CustomPlayerInventory {

	public MountSettingsInventory() {
		super("hub.items.mountsettingsinventory", 3);
	}

	@Override
	public void init(BadblockPlayer player) {
		HubPlayer hubPlayer = HubPlayer.get(player);
		BlueStainedGlassPaneItem blueStainedGlassPaneItem = new BlueStainedGlassPaneItem();
		this.setItem(blueStainedGlassPaneItem, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25);
		this.setItem(10, hubPlayer.getClickedMountItem());
		this.setItem(13, new MountSizeItem());
		this.setItem(14, new MountFunModeItem());
		this.setItem(15, new MountReverseModeItem());
		this.setItem(16, new MountPegasusModeItem());
		this.setItem(26, new BackMountsCosmeticsItem());
		this.setNoFilledItem(new CyanStainedGlassPaneItem());
	}

}

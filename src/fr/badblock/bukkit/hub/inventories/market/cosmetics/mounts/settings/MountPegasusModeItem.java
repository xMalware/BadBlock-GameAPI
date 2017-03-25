
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings;

import org.bukkit.Material;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomPlayerInventory;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults.MountConfig;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults.MountConfiguratorItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults.MountSettingsInventory;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.pegasus.PegasusWingsColorSelectorInventory;
import fr.badblock.gameapi.players.BadblockPlayer;

public class MountPegasusModeItem extends MountConfiguratorItem {

	public MountPegasusModeItem() {
		super("pegasusmode", Material.FEATHER, (byte) 0);
	}

	@Override
	public String getStateKey(MountConfig mountConfig) {
		return mountConfig.isPegasus() ? "hub.mounts.pegasusmode.enabled.lore" : "hub.mounts.pegasusmode.disabled.lore";
	}

	@Override
	public boolean isCompatible(MountItem mountItem) {
		return mountItem.hasPegasusMode();
	}

	@Override
	public void use(BadblockPlayer player, ItemAction itemAction, MountConfig mountConfig) {
		if (!player.hasPermission("hub.mounts.pegasusmode")) {
			player.sendTranslatedMessage("hub.mounts.pegasusmode.notpermission");
			return;
		}
		if (itemAction.equals(ItemAction.INVENTORY_WHEEL_CLICK)) {
			CustomInventory.get(PegasusWingsColorSelectorInventory.class).open(player);
			return;
		}
		if (mountConfig.isPegasus()) {
			mountConfig.setPegasus(false);
			player.sendTranslatedMessage("hub.mounts.pegasusmode.disabled");
			CustomPlayerInventory.get(MountSettingsInventory.class, player);
			return;
		}
		mountConfig.setPegasus(true);
		player.sendTranslatedMessage("hub.mounts.pegasusmode.enabled");
		CustomPlayerInventory.get(MountSettingsInventory.class, player);
	}

}

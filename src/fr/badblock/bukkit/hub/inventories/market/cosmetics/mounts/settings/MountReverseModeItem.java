
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings;

import org.bukkit.Material;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomPlayerInventory;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults.MountConfig;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults.MountConfiguratorItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults.MountSettingsInventory;
import fr.badblock.gameapi.players.BadblockPlayer;

public class MountReverseModeItem extends MountConfiguratorItem {

	public MountReverseModeItem() {
		super("reversemode", Material.SADDLE, (byte) 0);
	}

	@Override
	public String getStateKey(MountConfig mountConfig) {
		return mountConfig.isReverse() ? "hub.mounts.reversemode.enabled.lore" : "hub.mounts.reversemode.disabled.lore";
	}

	@Override
	public boolean isCompatible(MountItem mountItem) {
		return mountItem.hasReverseMode();
	}

	@Override
	public void use(BadblockPlayer player, ItemAction itemAction, MountConfig mountConfig) {
		if (!player.hasPermission("hub.mounts.reversemode")) {
			player.sendTranslatedMessage("hub.mounts.reversemode.notpermission");
			return;
		}
		if (mountConfig.isReverse()) {
			mountConfig.setReverse(false);
			player.sendTranslatedMessage("hub.mounts.reversemode.disabled");
			CustomPlayerInventory.get(MountSettingsInventory.class, player);
			return;
		}
		mountConfig.setReverse(true);
		player.sendTranslatedMessage("hub.mounts.reversemode.enabled");
		CustomPlayerInventory.get(MountSettingsInventory.class, player);
	}

}

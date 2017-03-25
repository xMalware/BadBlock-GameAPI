
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings;

import org.bukkit.Material;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomPlayerInventory;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults.MountConfig;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults.MountConfiguratorItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults.MountSettingsInventory;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.bukkit.hub.utils.MountManager;
import fr.badblock.gameapi.packets.watchers.WatcherSheep;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class MountSizeItem extends MountConfiguratorItem {

	public MountSizeItem() {
		super("sizemode", Material.GHAST_TEAR, (byte) 0);
	}

	@Override
	public String getStateKey(MountConfig mountConfig) {
		return mountConfig.isBaby() ? "hub.mounts.baby" : "hub.mounts.adult";
	}

	@Override
	public boolean isCompatible(MountItem mountItem) {
		return mountItem.hasResizableMode();
	}

	@Override
	public void use(BadblockPlayer player, ItemAction itemAction, MountConfig mountConfig) {
		if (!player.hasPermission("hub.soonbypass")) {
			player.sendTranslatedMessage("hub.items.functionsoon");
			return;
		}
		if (!player.hasPermission("hub.mounts.sizemode")) {
			player.sendTranslatedMessage("hub.mounts.sizemode.notpermission");
			return;
		}
		HubPlayer hubPlayer = HubPlayer.get(player);
		if (mountConfig.isBaby()) {
			mountConfig.setBaby(false);
			if (hubPlayer.getFakeEntity() != null) {
				hubPlayer.getFakeEntity().destroy();
				if (MountManager.rideEntity(player, hubPlayer.getFakeEntity().getType(), false, false, 0.3D, false, false)) {
					hubPlayer.setFakeEntity(null);
					player.sendTranslatedMessage("hub.mounts.nowababy");
					CustomPlayerInventory.get(MountSettingsInventory.class, player);
				}
			}
			return;
		}
		mountConfig.setBaby(true);
		if (hubPlayer.mountEntity != null) {
			hubPlayer.setFakeEntity(MountManager.spawn(player.getLocation(), hubPlayer.mountEntity.getType(), WatcherSheep.class, true, false, false, false, new TranslatableString(mountConfig.getMountName())));
			hubPlayer.mountEntity = null;
		}
		player.sendTranslatedMessage("hub.mounts.nowanadult");
		CustomPlayerInventory.get(MountSettingsInventory.class, player);
	}

}

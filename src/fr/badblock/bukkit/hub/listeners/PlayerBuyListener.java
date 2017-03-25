package fr.badblock.bukkit.hub.listeners;

import org.bukkit.event.EventHandler;

import fr.badblock.bukkit.hub.inventories.market.properties.CustomProperty;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.xmalware.badblock.shoplinker.api.ShopData;
import fr.xmalware.badblock.shoplinker.plugin.events.PlayerBuyEvent;

public class PlayerBuyListener extends _HubListener {

	private final static String SEPARATOR = "_";
	
	@EventHandler (ignoreCancelled = false)
	public void onPlayerBuyListener(PlayerBuyEvent event) {
		ShopData shopData = event.getShopData();
		String objectName = shopData.getObjectName();
		// No separator
		if (!objectName.contains(SEPARATOR)) return;
		String propertyData = objectName.split(SEPARATOR)[1];
		if (!CustomProperty.isACustomProperty(objectName)) {
			System.out.println("[HUB] " + objectName + " isn't a property.");
			return;
		}
		CustomProperty customProperty = CustomProperty.getPropertyTypeByName(objectName);
		BadblockPlayer player = BukkitUtils.getPlayer(shopData.getPlayerName());
		if (player == null) return;
		customProperty.run(player, propertyData);
	}

}

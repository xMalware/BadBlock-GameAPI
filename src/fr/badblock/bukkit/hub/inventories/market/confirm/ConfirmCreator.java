package fr.badblock.bukkit.hub.inventories.market.confirm;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomPlayerInventory;
import fr.badblock.bukkit.hub.inventories.market.ownitems.OwnableItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class ConfirmCreator {

	public static void buy(BadblockPlayer player, OwnableItem ownableItem) {
		if (ownableItem.has(player)) {
			player.sendTranslatedMessage("hub.alreadyowned", player.getTranslatedMessage(ownableItem.getName())[0]);
			return;
		}
		if (ownableItem.hasPermission() && !player.hasPermission("hub." + ownableItem.getConfigPrefix())) {
			player.sendTranslatedMessage("hub.nopermission", player.getTranslatedMessage(ownableItem.getName())[0]);
			return;
		}
		int badcoins = player.getPlayerData().getBadcoins();
		if (badcoins < ownableItem.getNeededBadcoins()) {
			player.sendTranslatedMessage("hub.notenoughbadcoins", ownableItem.getNeededBadcoins() - badcoins);
			return;
		}
		HubPlayer hubPlayer = HubPlayer.get(player);
		hubPlayer.setBuyItem(ownableItem);
		CustomPlayerInventory.get(ConfirmCreatorInventory.class, player).open();
	}
	
}

package fr.badblock.bukkit.hub.inventories.market.properties.runnables;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.market.properties.CustomPropertyRunnable;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.data.PlayerData;
import fr.badblock.gameapi.utils.ConfigUtils;

public class BadCoinsCustomPropertyRunnable extends CustomPropertyRunnable {

	@Override
	public void work(BadblockPlayer player, String ownItem) {
		int result = Integer.parseInt(ownItem);
		PlayerData gamePlayerData = player.getPlayerData();
		gamePlayerData.addBadcoins(result, false);
		player.saveGameData();
	}

	@Override
	public String getCustomI18n(BadblockPlayer player, String ownItem) {
		return ownItem;
	}

	@Override
	public ItemStack getItemStack(BadblockPlayer player, String ownItem) {
		return new ItemStack(Material.getMaterial(ConfigUtils.get(BadBlockHub.getInstance(), "chests.winitem.badcoins.type", Material.GOLD_INGOT.name())), ConfigUtils.getInt(BadBlockHub.getInstance(), "chests.winitem.badcoins.amount"), (byte) ConfigUtils.getInt(BadBlockHub.getInstance(), "chests.winitem.badcoins.data"));
	}

}

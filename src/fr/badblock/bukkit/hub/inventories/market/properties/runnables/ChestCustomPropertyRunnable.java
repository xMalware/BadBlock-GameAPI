package fr.badblock.bukkit.hub.inventories.market.properties.runnables;

import org.bukkit.inventory.ItemStack;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.chests.objects.ChestLoader;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.chests.objects.CustomChest;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.chests.objects.CustomChestType;
import fr.badblock.bukkit.hub.inventories.market.properties.CustomPropertyRunnable;
import fr.badblock.bukkit.hub.objects.HubStoredPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.data.PlayerData;

public class ChestCustomPropertyRunnable extends CustomPropertyRunnable {

	@Override
	public void work(BadblockPlayer player, String ownItem) {
		PlayerData gamePlayerData = player.getPlayerData();
		HubStoredPlayer hubStoredPlayer = gamePlayerData.gameData("hub", HubStoredPlayer.class);
		hubStoredPlayer.getChests().add(new CustomChest(Integer.parseInt(ownItem), false));
		player.saveGameData();
	}

	@Override
	public String getCustomI18n(BadblockPlayer player, String ownItem) {
		return player.getTranslatedMessage("hub.chests." + ownItem + ".name")[0];
	}

	@Override
	public ItemStack getItemStack(BadblockPlayer player, String ownItem) {
		CustomChestType customChest = null;
		for (CustomChestType customChestt : ChestLoader.getInstance().getChests())
			if (customChestt.getId() == Integer.parseInt(ownItem)) customChest = customChestt;
		return customChest.getItemStack();
	}

}

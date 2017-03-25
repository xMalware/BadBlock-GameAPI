package fr.badblock.bukkit.hub.inventories.settings.settings.clans;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.gameapi.players.BadblockPlayer;

public class CanJoinWhileRunningSettingsItem extends CustomItem {

	public CanJoinWhileRunningSettingsItem() {
		// super("§bMessages privés", Material.PAPER);
		super("hub.items.canjoinwhilerunningsettingsitem", Material.REDSTONE_TORCH_ON, "hub.items.canjoinwhilerunningsettingsitem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.closeInventory();
		if (!player.canOnlyJoinWhileWaiting()) {
			player.setOnlyJoinWhileWaiting(System.currentTimeMillis() + 604_800_000L);
			player.sendTranslatedMessage("hub.items.canjoinwhilerunningsettingsitem.enabled");
			player.playSound(Sound.LEVEL_UP);
			player.saveGameData();
		}else{
			player.setOnlyJoinWhileWaiting(-1);
			player.sendTranslatedMessage("hub.items.canjoinwhilerunningsettingsitem.disabled");
			player.playSound(Sound.LEVEL_UP);
			player.saveGameData();
		}
	}
	
	@Override
	public ItemStack toItemStack(BadblockPlayer player) {
		return build(this.getMaterial(), this.getAmount(), this.getData(),
				player.getTranslatedMessage("hub.items.canjoinwhilerunningsettingsitem", player.canOnlyJoinWhileWaiting() ? player.getTranslatedMessage("hub.items.canjoinwhilerunningsettingsitem.enabled_lore")[0] :
					player.getTranslatedMessage("hub.items.canjoinwhilerunningsettingsitem.disabled_lore")[0])[0],
				player.getTranslatedMessage("hub.items.canjoinwhilerunningsettingsitem.lore", player.canOnlyJoinWhileWaiting() ? player.getTranslatedMessage("hub.items.canjoinwhilerunningsettingsitem.enabled_lore")[0] :
					player.getTranslatedMessage("hub.items.canjoinwhilerunningsettingsitem.disabled_lore")[0]));
	}

}
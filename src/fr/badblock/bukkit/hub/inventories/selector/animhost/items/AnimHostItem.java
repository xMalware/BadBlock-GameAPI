package fr.badblock.bukkit.hub.inventories.selector.animhost.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.BadblockGame;

public abstract class AnimHostItem extends CustomItem {

	private String 		 systemName;
	
	@SuppressWarnings("deprecation")
	public AnimHostItem(String string, BadblockGame game, String systemName) {
		super(string, game.getItemStackFactory().create(1).getType(), game.getItemStackFactory().create(1).getData().getData());
		this.systemName = systemName;
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.closeInventory();
		AnimHostManager.openServer(player, systemName);
	}
	
}

package fr.badblock.bukkit.hub.inventories.selector.items.special;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.gameapi.players.BadblockPlayer;

public class DescSelectorItem extends CustomItem {

	public DescSelectorItem(String gameName, Material material) {
		super("hub.items." + gameName + ".desc", material, "hub.items." + gameName + ".desc.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList();
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {

	}

}

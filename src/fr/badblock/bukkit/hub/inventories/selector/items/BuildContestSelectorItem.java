package fr.badblock.bukkit.hub.inventories.selector.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.selector.submenus.inventories.BuildContestChooserInventory;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.ConfigUtils;

public class BuildContestSelectorItem extends GameSelectorItem {

	private static ItemStack itemStack = BadblockGame.BUILDCONTEST.createItemStack();
	
	public BuildContestSelectorItem() {
		super("hub.items.buildcontestselectoritem", itemStack.getType(), "hub.items.buildcontestselectoritem.lore");
		this.setFakeEnchantment(true);
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public List<String> getGames() {
		return Arrays.asList("buildcontest16");
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		if (itemAction.equals(ItemAction.INVENTORY_LEFT_CLICK)) {
			CustomInventory.get(BuildContestChooserInventory.class).open(player);
			return;
		}
		Location location = ConfigUtils.getLocation(BadBlockHub.getInstance(), "buildcontest");
		if (location == null)
			player.sendTranslatedMessage("hub.gameunavailable");
		else
			player.teleport(location);
	}

	@Override
	public BadblockGame getGame() {
		return BadblockGame.BUILDCONTEST;
	}

	@Override
	public boolean isMiniGame() {
		return true;
	}

	@Override
	public String getGamePrefix() {
		return "buildcontest";
	}

}

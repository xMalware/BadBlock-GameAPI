package fr.badblock.bukkit.hub.inventories.selector.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.selector.submenus.inventories.PearlsWarChooserInventory;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.ConfigUtils;

public class PearlsWarSelectorItem extends GameSelectorItem {

	public PearlsWarSelectorItem() {
		// super("§bPearlsWar", Material.ENDER_PEARL);
		super("hub.items.pearlswarselectoritem", Material.ENDER_PEARL, "hub.items.pearlswarsselectoritem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public List<String> getGames() {
		return Arrays.asList("pw16");
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		if (itemAction.equals(ItemAction.INVENTORY_LEFT_CLICK)) {
			CustomInventory.get(PearlsWarChooserInventory.class).open(player);
			return;
		}
		Location location = ConfigUtils.getLocation(BadBlockHub.getInstance(), "pearlswar");
		if (location == null) // player.sendMessage("§cCe jeu est
								// indisponible.");
			player.sendTranslatedMessage("hub.gameunavailable");
		else
			player.teleport(location);
	}

	@Override
	public BadblockGame getGame() {
		return BadblockGame.PEARLSWAR;
	}

	@Override
	public boolean isMiniGame() {
		return false;
	}
	
	@Override
	public String getGamePrefix() {
		return "pw";
	}

}

package fr.badblock.bukkit.hub.inventories.selector.items;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.utils.TimeUtils;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.ConfigUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class FreeBuildSelectorItem extends GameSelectorItem {

	public FreeBuildSelectorItem() {
		// super("§bFreeBuild", Material.BRICK);
		super("hub.items.freebuildselectoritem", Material.BRICK, "hub.items.freebuildselectoritem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public List<String> getGames() {
		return Arrays.asList("fb", "fb2");
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		Location location = ConfigUtils.getLocation(BadBlockHub.getInstance(), "freebuild");
		if (location == null)
			player.sendTranslatedMessage("hub.gameunavailable");
		else {
			if (itemAction.equals(ItemAction.INVENTORY_LEFT_CLICK)) {
				//CustomInventory.get(FreeBuildChooserInventory.class).open(player);
				player.sendMessage("§b▶ §7Entrée §bsolitaire §7dans la file §b(FreeBuild Alpha)§7...");
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						player.sendPlayer("fb");
						/*if (!player.hasPermission("others.mod.connect")) {
							SEntryInfosListener.tempPlayers.put(player.getName(), System.currentTimeMillis() + SEntryInfosListener.tempTime);
							SEntryInfosListener.tempPlayersRank.put(player.getName(), player.getMainGroup());
							SEntryInfosListener.tempPlayersUUID.put(player.getName(), player.getUniqueId());
							SEntryInfosListener.tempPlayersPropertyMap.put(player.getName(), ((CraftPlayer)player).getHandle().getProfile().getProperties());
						}*/
					}
				};
				if (player.hasPermission("matchmaking.priority")) {
					player.sendMessage("§b➤ §7Téléportation §bsolitaire §7en jeu §b(FreeBuild Alpha)§7...");
					runnable.run();
				}
				else {
					player.sendMessage("§b▶ §7Entrée §bsolitaire §7dans la file §b(FreeBuild Alpha)§7...");
					int time = new Random().nextInt(20 * 9) + (20 * 3);
					player.sendMessage("§bAccès: §3Standard §b| Estimé: §b" + TimeUtils.getStringTime(time / 20));
					TaskManager.runAsyncTaskLater(runnable, time);
				}
				player.closeInventory();
				return;
			}
			player.teleport(location);
		}
	}

	@Override
	public BadblockGame getGame() {
		return BadblockGame.FREEBUILD;
	}

	@Override
	public boolean isMiniGame() {
		return false;
	}

	@Override
	public String getGamePrefix() {
		return "fb";
	}

}

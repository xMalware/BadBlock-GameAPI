package fr.badblock.bukkit.hub.inventories.selector.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.gson.Gson;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.ConfigUtils;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.rabbitconnector.RabbitService;
import fr.badblock.sentry.SEntry;
import fr.badblock.utils.Encodage;

public class RushSelectorItem extends GameSelectorItem {

	public RushSelectorItem() {
		// super("§bRush", Material.BED);
		super("hub.items.rushselectoritem", Material.BED, "hub.items.rushselectoritem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public List<String> getGames() {
		return Arrays.asList("rush", "rush2v2", "rush4v4", "rush4x4", "rush2v2wb", "rush4v4wb", "rush4x4wb", "rush4x1");
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		if (itemAction.equals(ItemAction.INVENTORY_LEFT_CLICK)) {
			/*CustomInventory.get(RushChooserInventory.class).open(player);*/
			BadBlockHub instance = BadBlockHub.getInstance();
			RabbitService service = instance.getRabbitService();
			Gson gson = instance.getGson();
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					service.sendAsyncPacket("networkdocker.sentry.join", gson.toJson(new SEntry(player.getName(), "rush", false)),
							Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
				}
			};
			if (player.hasPermission("matchmaking.priority")) runnable.run();
			else {
				runnable.run();//TaskManager.runAsyncTaskLater(runnable, new Random().nextInt(20 * 9) + (20 * 3));
			}
			player.closeInventory();
			return;
		}
		Location location = ConfigUtils.getLocation(BadBlockHub.getInstance(), "rush");
		if (location == null) // player.sendMessage("§cCe jeu est
								// indisponible.");
			player.sendTranslatedMessage("hub.gameunavailable");
		else
			player.teleport(location);
	}

	@Override
	public BadblockGame getGame() {
		return BadblockGame.RUSH;
	}

	@Override
	public boolean isMiniGame() {
		return true;
	}
	
	@Override
	public String getGamePrefix() {
		return "rush";
	}

}

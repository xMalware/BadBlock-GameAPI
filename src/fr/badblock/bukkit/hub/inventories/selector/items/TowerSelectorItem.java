package fr.badblock.bukkit.hub.inventories.selector.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.gson.Gson;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.rabbitconnector.RabbitService;
import fr.badblock.sentry.SEntry;
import fr.badblock.utils.Encodage;

public class TowerSelectorItem extends GameSelectorItem {

	public TowerSelectorItem() {
		// super("§bTower", Material.NETHER_FENCE);
		super("hub.items.towerselectoritem", Material.NETHER_FENCE, "hub.items.towerselectoritem.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public List<String> getGames() {
		return Arrays.asList("tower", "tower2v2", "tower4v4", "tower8v8", "tower2v2nb", "tower4v4nb", "tower8v8nb");
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		if (itemAction.equals(ItemAction.INVENTORY_LEFT_CLICK)) {
			//CustomInventory.get(TowerChooserInventory.class).open(player);
			BadBlockHub instance = BadBlockHub.getInstance();
			RabbitService service = instance.getRabbitService();
			Gson gson = instance.getGson();
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					service.sendAsyncPacket("networkdocker.sentry.join", gson.toJson(new SEntry(player.getName(), "tower", false)),
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
		/*Location location = ConfigUtils.getLocation(BadBlockHub.getInstance(), "tower");
		if (location == null) // player.sendMessage("§cCe jeu est
								// indisponible.");
			player.sendTranslatedMessage("hub.gameunavailable");
		else{
			player.teleport(location);
		}*/
	}

	@Override
	public BadblockGame getGame() {
		return BadblockGame.TOWER;
	}

	@Override
	public boolean isMiniGame() {
		return true;
	}
	
	@Override
	public String getGamePrefix() {
		return "tower";
	}

}

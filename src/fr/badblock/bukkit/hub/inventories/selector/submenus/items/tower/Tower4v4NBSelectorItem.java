package fr.badblock.bukkit.hub.inventories.selector.submenus.items.tower;

import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import com.google.gson.Gson;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.SubGameSelectorItem;
import fr.badblock.bukkit.hub.rabbitmq.SEntryInfosListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.rabbitconnector.RabbitService;
import fr.badblock.sentry.SEntry;
import fr.badblock.utils.Encodage;

public class Tower4v4NBSelectorItem extends SubGameSelectorItem {

	@SuppressWarnings("deprecation")
	public Tower4v4NBSelectorItem() {
		super("hub.items.towerselectoritem.4v4nb", Material.BANNER, DyeColor.RED.getDyeData(),
				"hub.items.towerselectoritem.4v4nb.lore");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public List<String> getGames() {
		return Arrays.asList("tower4v4nb");
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		BadBlockHub instance = BadBlockHub.getInstance();
		RabbitService service = instance.getRabbitService();
		Gson gson = instance.getGson();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				service.sendAsyncPacket("networkdocker.sentry.join", gson.toJson(new SEntry(player.getName(), "tower4v4nb", false)),
						Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
				if (!player.hasPermission("others.mod.connect")) {
					SEntryInfosListener.tempPlayers.put(player.getName(), System.currentTimeMillis() + SEntryInfosListener.tempTime);
					SEntryInfosListener.tempPlayersRank.put(player.getName(), player.getMainGroup());
					SEntryInfosListener.tempPlayersUUID.put(player.getName(), player.getUniqueId());
					SEntryInfosListener.tempPlayersPropertyMap.put(player.getName(), ((CraftPlayer)player).getHandle().getProfile().getProperties());
				}
			}
		};
		if (player.hasPermission("matchmaking.priority")) runnable.run();
		else {
			runnable.run();//TaskManager.runAsyncTaskLater(runnable, new Random().nextInt(20 * 9) + (20 * 3));
		}
		player.closeInventory();
	}

	@Override
	public boolean isMiniGame() {
		return true;
	}

}

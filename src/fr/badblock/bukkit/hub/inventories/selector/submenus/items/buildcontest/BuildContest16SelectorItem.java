package fr.badblock.bukkit.hub.inventories.selector.submenus.items.buildcontest;

import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.gson.Gson;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.selector.submenus.items.SubGameSelectorItem;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.rabbitconnector.RabbitService;
import fr.badblock.sentry.SEntry;
import fr.badblock.utils.Encodage;

public class BuildContest16SelectorItem extends SubGameSelectorItem {

	@SuppressWarnings("deprecation")
	public BuildContest16SelectorItem() {
		super("hub.items.buildcontestselectoritem.16", Material.BANNER, DyeColor.ORANGE.getDyeData(), "hub.items.buildcontestselectoritem.16.lore");
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
		BadBlockHub instance = BadBlockHub.getInstance();
		RabbitService service = instance.getRabbitService();
		Gson gson = instance.getGson();
		service.sendAsyncPacket("networkdocker.sentry.join", gson.toJson(new SEntry(player.getName(), "buildcontest16", false)), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
		player.closeInventory();
	}

	@Override
	public boolean isMiniGame() {
		return true;
	}

}

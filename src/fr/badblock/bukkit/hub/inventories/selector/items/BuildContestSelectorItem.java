package fr.badblock.bukkit.hub.inventories.selector.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

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
			//CustomInventory.get(BuildContestChooserInventory.class).open(player);
			BadBlockHub instance = BadBlockHub.getInstance();
			RabbitService service = instance.getRabbitService();
			Gson gson = instance.getGson();
			service.sendAsyncPacket("networkdocker.sentry.join", gson.toJson(new SEntry(player.getName(), "buildcontest16", false)), Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
			player.closeInventory();
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

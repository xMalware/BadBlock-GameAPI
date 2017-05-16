package fr.badblock.bukkit.hub.inventories.settings.statistics;

import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.inventories.settings.StatisticsInventory;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.general.CalcUtil;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class BuildContestStatisticsItem extends CustomItem {

	public BuildContestStatisticsItem() {
		super("hub.items.buildcontestselectoritem", BadblockGame.BUILDCONTEST.createItemStack().getType());
		this.setFakeEnchantment(true);
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		StatisticsInventory.openAchievements(player, BadblockGame.BUILDCONTEST);
	}

	@Override
	public ItemStack toItemStack(BadblockPlayer player) {
		TranslatableString prefix = new TranslatableString("hub.items.buildcontestselectoritem");
		ItemStack itemStack = build(this.getMaterial(), 1, (byte) 0, prefix.getAsLine(player),
				player.getTranslatedMessage("hub.items.buildconteststatistics",
						CalcUtil.getInstance().convertInt(player.getPlayerData().getStatistics("buildcontest", "wins")),
						CalcUtil.getInstance().convertInt(player.getPlayerData().getStatistics("buildcontest", "looses"))));
		return itemStack;
	}

}

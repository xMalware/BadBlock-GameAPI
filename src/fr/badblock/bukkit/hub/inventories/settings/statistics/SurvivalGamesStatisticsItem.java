package fr.badblock.bukkit.hub.inventories.settings.statistics;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.inventories.settings.StatisticsInventory;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.run.BadblockGame;
import fr.badblock.gameapi.utils.general.CalcUtil;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class SurvivalGamesStatisticsItem extends CustomItem {

	public SurvivalGamesStatisticsItem() {
		// super("§bSurvivalGames", Material.IRON_SWORD);
		super("hub.items.survivalgamesselectoritem", Material.IRON_SWORD);
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		StatisticsInventory.openAchievements(player, BadblockGame.SURVIVAL_GAMES);
	}

	@Override
	public ItemStack toItemStack(BadblockPlayer player) {
		TranslatableString prefix = new TranslatableString("hub.items.survivalgamesselectoritem");
		ItemStack itemStack = build(Material.IRON_SWORD, 1, (byte) 0, prefix.getAsLine(player),
				player.getTranslatedMessage("hub.items.survivalgamesstatistics",
						CalcUtil.getInstance()
								.convertInt(player.getPlayerData().getStatistics("survival", "wins")),
						CalcUtil.getInstance()
								.convertInt(player.getPlayerData().getStatistics("survival", "kills")),
						CalcUtil.getInstance()
								.convertInt(player.getPlayerData().getStatistics("survival", "looses")),
						CalcUtil.getInstance()
								.convertInt(player.getPlayerData().getStatistics("survival", "deaths")),
						CalcUtil.getInstance().getRatio(player.getPlayerData().getStatistics("survival", "kills"),
								player.getPlayerData().getStatistics("survival", "deaths"))));
		return itemStack;
	}

}

package fr.badblock.bukkit.hub.inventories.join.items;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.bukkit.hub.objects.HubStoredPlayer;
import fr.badblock.bukkit.hub.rabbitmq.SEntryInfosListener;
import fr.badblock.bukkit.hub.utils.NPC;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import lombok.Setter;

public class HiderPlayerItem extends CustomItem {

	@Getter
	@Setter
	public static HiderDisablePlayerItem disabler = HiderDisablePlayerItem.getInstance();
	@Setter
	public static HiderPlayerItem instance;

	public static HiderPlayerItem getInstance() {
		return instance == null ? instance = new HiderPlayerItem() : instance;
	}

	@SuppressWarnings("deprecation")
	public HiderPlayerItem() {
		super("hub.items.hiderplayeritem", Material.getMaterial(351), (byte) 10, "hub.items.hiderplayeritem.lore");
		// super("§7Masquer les joueurs §7(clic droit)",
		// Material.getMaterial(351), (byte) 10, "", "§c> §bMasquez §7les
		// joueurs§7,", "§7en ne laissant que le §7staff", "§7et §bvos amis §7à
		// votre portée !");
		setInstance(this);
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK, ItemAction.RIGHT_CLICK_AIR,
				ItemAction.RIGHT_CLICK_BLOCK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 2));
		player.playSound(player.getLocation(), Sound.EXPLODE, 100, 1);
		HubStoredPlayer hubStoredPlayer = HubStoredPlayer.get(player);
		hubStoredPlayer.setHidePlayers(true);
		// Reformate content
		ItemStack[] content = player.getInventory().getContents();
		for (int i = 0; i < content.length; i++) {
			ItemStack itemStack = content[i];
			if (itemStack != null && itemStack.isSimilar(this.getStaticItem().get(player.getPlayerData().getLocale())))
				content[i] = disabler.getStaticItem().get(player.getPlayerData().getLocale());
		}
		player.getInventory().setContents(content);
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (pl.hasPermission("hub.bypasshide"))
				continue;
			if (player.inGameData(HubPlayer.class).getFriends().contains(pl.getName()))
				continue;
			if (!player.canSee(pl))
				continue;
			player.hidePlayer(pl);
		}
		for (NPC npc : SEntryInfosListener.tempNPCs.values()) {
			npc.despawn(player);
		}
		player.saveGameData();
		// player.sendMessage("§7Vous avez §ccaché§7 les joueurs.");
		player.sendTranslatedMessage("hub.items.hiderplayeritem.success");
	}

}

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
import fr.badblock.bukkit.hub.objects.HubStoredPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import lombok.Setter;

public class HiderDisablePlayerItem extends CustomItem {

	@Getter
	@Setter
	public static HiderPlayerItem enabler = HiderPlayerItem.getInstance();
	@Setter
	public static HiderDisablePlayerItem instance;

	public static HiderDisablePlayerItem getInstance() {
		return instance == null ? instance = new HiderDisablePlayerItem() : instance;
	}

	@SuppressWarnings("deprecation")
	public HiderDisablePlayerItem() {
		// super("§aAfficher les joueurs §7(clic droit)",
		// Material.getMaterial(351), (byte) 8, "", "§c> §bAffichez tous §7les
		// joueurs§7");
		super("hub.items.hiderdisableplayeritem", Material.getMaterial(351), (byte) 8,
				"hub.items.hiderdisableplayeritem.lore");
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
		player.playSound(player.getLocation(), Sound.LEVEL_UP, 100, 1);
		HubStoredPlayer hubStoredPlayer = HubStoredPlayer.get(player);
		hubStoredPlayer.setHidePlayers(false);
		// Reformate content
		ItemStack[] content = player.getInventory().getContents();
		for (int i = 0; i < content.length; i++) {
			ItemStack itemStack = content[i];
			if (itemStack != null && itemStack.isSimilar(this.getStaticItem().get(player.getPlayerData().getLocale())))
				content[i] = enabler.getStaticItem().get(player.getPlayerData().getLocale());
		}
		player.getInventory().setContents(content);
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if (player.canSee(pl))
				continue;
			player.showPlayer(pl);
		}
		/*for (Entry<String, NPC> entry : SEntryInfosListener.tempNPCs.entrySet()) {
			if (SEntryInfosListener.tempPlayersUUID.containsKey(entry.getKey()) && SEntryInfosListener.tempPlayersPropertyMap.containsKey(entry.getKey()))
				entry.getValue().show(entry.getKey(), SEntryInfosListener.tempPlayersUUID.get(entry.getKey()), player, SEntryInfosListener.tempPlayersPropertyMap.get(entry.getKey()));
		}*/
		player.saveGameData();
		player.sendTranslatedMessage("hub.items.hiderdisableplayeritem.success");
	}

}

package fr.badblock.game.core18R3.itemstack;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;

import fr.badblock.game.core18R3.players.ingamedata.CommandInGameData;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;
import net.md_5.bungee.api.ChatColor;

public class ItemStackExtras implements Listener {
	private static ItemStackExtras instance;

	public static ItemStackExtras get(){ return instance; }

	private Map<Integer, GameItemExtra> items;
	private int NEXT_ID = 0;

	public ItemStackExtras(){
		instance = this;
		Bukkit.getPluginManager().registerEvents(this, GameAPI.getAPI());

		this.items = Maps.newConcurrentMap();
	}

	public int register(GameItemExtra extra){
		int id = NEXT_ID++;
		items.put(id, extra);
		return id;
	}

	public void unregister(int id){
		items.remove(id);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		GameItemExtra extra = getExtra(e.getItem());
		if(extra == null) return;

		ItemAction action = ItemAction.get(e.getAction());

		if(action != null){
			ItemEvent event = extra.getItems().get(action);

			if(event != null){
				boolean cancel = event.call(action, player);

				if(cancel){
					e.setUseInteractedBlock(Result.DENY);
					e.setUseItemInHand(Result.DENY);

					if(e.getAction() == Action.RIGHT_CLICK_BLOCK)
						new BukkitRunnable(){
							@Override
							public void run(){
								e.getPlayer().updateInventory();
							}
						}.runTaskLater(GameAPI.getAPI(), 1L);
					
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		GameItemExtra extra = getExtra(e.getItemDrop().getItemStack());
		if(extra == null) return;

		ItemAction action = ItemAction.DROP;
		ItemEvent event = extra.getItems().get(action);

		if(event != null){
			boolean cancel = event.call(action, player);

			if(cancel){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(e.getWhoClicked().getType() != EntityType.PLAYER) return;
		
		BadblockPlayer player = (BadblockPlayer) e.getWhoClicked();

		if(player.inGameData(CommandInGameData.class).invsee){
			if(!player.hasPermission(GamePermission.ADMIN))
				e.setCancelled(true);
			return;
		}
		
		if(player.isJailed()) {
			e.setCancelled(true);
			player.closeInventory();
			return;
		}
		
		test(e.getCurrentItem(), player, e);

		if(e.getClickedInventory() != null && e.getCurrentItem() == null) {
			ItemStack is = e.getClickedInventory().getItem(e.getSlot());
			test(is, player, e);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e){
		if(e.getPlayer().getType() != EntityType.PLAYER) return;
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		player.inGameData(CommandInGameData.class).invsee = false;
	}

	public void test(ItemStack is, BadblockPlayer player, InventoryClickEvent e){
		GameItemExtra extra = getExtra(is);
		if(extra == null) return;

		ItemAction action = ItemAction.get(e.getAction());

		if(action != null){
			ItemEvent event = extra.getItems().get(action);

			if(event != null){
				boolean cancel = event.call(action, player);

				if(cancel)
					e.setCancelled(true);
			}
		}
	}

	public static String encodeInName(String itemName, int itemID) {
		String id = Integer.toString(itemID);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < id.length(); i++) {
			builder.append(ChatColor.COLOR_CHAR).append(id.charAt(i));
		}
		String result = builder.toString() + ChatColor.COLOR_CHAR + "S" + itemName;
		return result;
	}

	public static int decodeId(String itemName) {
		int intId = -1;
		if (itemName.contains(ChatColor.COLOR_CHAR + "S")) {
			String[] stringID = itemName.split(ChatColor.COLOR_CHAR + "S");
			if (stringID.length > 0) {
				itemName = stringID[0].replaceAll(ChatColor.COLOR_CHAR + "", "");
				try {
					intId = Integer.parseInt(itemName);
				} catch (NumberFormatException unused){}
			}
		}
		return intId;
	}

	public static GameItemExtra getExtra(ItemStack item){
		if(item == null || item.getItemMeta() == null) return null;
		int id = -1;
		
		if(!item.getItemMeta().hasDisplayName()){
			if(item.getItemMeta().getLore() != null && !item.getItemMeta().getLore().isEmpty()){
				id = decodeId(item.getItemMeta().getLore().get(0));
			}

		} else id = decodeId(item.getItemMeta().getDisplayName());

		if(id == -1 || !get().items.containsKey(id)) return null;


		GameItemExtra extra = get().items.get(id);
		return extra;
	}
}

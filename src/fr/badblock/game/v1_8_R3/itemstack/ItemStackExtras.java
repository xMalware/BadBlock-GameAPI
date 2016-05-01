package fr.badblock.game.v1_8_R3.itemstack;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;
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

		if(ItemStackUtils.hasDisplayname(e.getItem())){
			int id = decodeId(e.getItem().getItemMeta().getDisplayName());

			if(id == -1 || !items.containsKey(id)) return;

			
			GameItemExtra extra = items.get(id);

			ItemAction action = ItemAction.get(e.getAction());
			
			if(action != null){
				ItemEvent event = extra.getItems().get(action);
				
				if(event != null){
					boolean cancel = event.call(action, player);
				
					if(cancel){
						e.setUseInteractedBlock(Result.DENY);
						e.setUseItemInHand(Result.DENY);
						
						e.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(e.getWhoClicked().getType() != EntityType.PLAYER) return;
		BadblockPlayer player = (BadblockPlayer) e.getWhoClicked();

		test(e.getCurrentItem(), player, e);
		
		if(e.getClickedInventory() != null)
			test(e.getClickedInventory().getItem(e.getSlot()), player, e);
	}
	
	private void test(ItemStack is, BadblockPlayer player, InventoryClickEvent e){
		if(ItemStackUtils.hasDisplayname(is)){
			int id = decodeId(is.getItemMeta().getDisplayName());
			if(id == -1 || !items.containsKey(id)) return;
			
			GameItemExtra extra = items.get(id);
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
}

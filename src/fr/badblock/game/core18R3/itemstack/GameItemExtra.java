package fr.badblock.game.core18R3.itemstack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;
import lombok.Data;
import lombok.experimental.Accessors;

@Data@Accessors(chain=true) public class GameItemExtra implements ItemStackExtra {
	private Map<ItemAction, ItemEvent> items;

	private ItemStack handler		 = null;
	private boolean listening		 = false;

	private int     id				 = 0;
	
	private boolean allowDropOnDeath = true;
	
	public GameItemExtra(ItemStack handler){
		if(!ItemStackUtils.isValid(handler)){
			throw new IllegalArgumentException("Handler must be valid!");
		}
		
		this.handler = handler;
		this.items   = Maps.newConcurrentMap();
		this.id		 = ItemStackExtras.get().register(this);
		
		if(!handler.getItemMeta().hasDisplayName()){
			setLore(handler.getItemMeta().getLore());
		} else setDisplayName(handler.getItemMeta().getDisplayName());
	}
	
	@Override
	public void stopListeningAt() {
		this.listening = false;
	}
	
	@Override
	public ItemStackExtra setDisplayName(String displayName){
		displayName = ItemStackUtils.encodeIDInName(displayName, id);
		
		ItemMeta meta = handler.getItemMeta();
		meta.setDisplayName(displayName);
		handler.setItemMeta(meta);
		
		return this;
	}
	
	private void setLore(List<String> lore){
		if(lore == null)
			lore = new ArrayList<>();
		
		if(lore.isEmpty()){
			lore.add(ItemStackUtils.encodeIDInName("", id));
		} else {
			String[] res = lore.toArray(new String[0]);
			
			res[0] = ItemStackUtils.encodeIDInName(res[0], id);
			lore = Arrays.asList(res);
		}
		
		ItemMeta meta = handler.getItemMeta();
		meta.setLore(lore);
		handler.setItemMeta(meta);
	}

	@Override
	public ItemStackExtra allowDropOnDeath(boolean can) {
		this.allowDropOnDeath = can;
		return this;
	}
	
	@Override
	public ItemStackExtra allow(ItemAction... action) {
		listen(new ItemEvent(){
			@Override
			public boolean call(ItemAction action, BadblockPlayer player) {
				return false;
			}
		}, action);
		
		return this;
	}

	@Override
	public ItemStackExtra disallow(ItemAction... action) {
		listen(new ItemEvent(){
			@Override
			public boolean call(ItemAction action, BadblockPlayer player) {
				return true;
			}
		}, action);
		
		return this;
	}

	@Override
	public ItemStackExtra listen(ItemEvent event, ItemAction... actions) {
		for(ItemAction action : actions)
			items.put(action, event);
		return this;
	}

	@Override
	public ItemStackExtra listenAs(ItemEvent optionalEvent, ItemPlaces place) {
		items.clear();
		
		if(place != ItemPlaces.NORMAL){
			disallow(ItemAction.DROP, ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_WHEEL_CLICK);
		}
		
		if(place == ItemPlaces.HOTBAR_CLICKABLE || place == ItemPlaces.INVENTORY_CLICKABLE){
			listen(optionalEvent, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK,
					ItemAction.LEFT_CLICK_AIR, ItemAction.LEFT_CLICK_BLOCK,
					ItemAction.RIGHT_CLICK_AIR, ItemAction.RIGHT_CLICK_BLOCK);
		}
		
		if(place == ItemPlaces.HOTBAR_UNCLICKABLE || place == ItemPlaces.INVENTORY_UNCLICKABLE){
			disallow(ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK,
					ItemAction.LEFT_CLICK_AIR, ItemAction.LEFT_CLICK_BLOCK,
					ItemAction.RIGHT_CLICK_AIR, ItemAction.RIGHT_CLICK_BLOCK);
		}
		
		return this;
	}
}

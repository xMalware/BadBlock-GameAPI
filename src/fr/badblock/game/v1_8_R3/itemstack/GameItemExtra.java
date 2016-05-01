package fr.badblock.game.v1_8_R3.itemstack;

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
	
	public GameItemExtra(ItemStack handler){
		if(!ItemStackUtils.hasDisplayname(handler)){
			throw new IllegalArgumentException("Handler must have a display name!");
		}
		
		this.handler = handler;
		this.items   = Maps.newConcurrentMap();
		this.id		 = ItemStackExtras.get().register(this);
		
		setDisplayName(handler.getItemMeta().getDisplayName());
	}
	
	@Override
	public void stopListeningAt() {
		this.listening = false;
	}
	
	@Override
	public ItemStackExtra setDisplayName(String displayName){
		ItemMeta meta = handler.getItemMeta();
		meta.setDisplayName(ItemStackExtras.encodeInName(displayName, id));
		handler.setItemMeta(meta);
		
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

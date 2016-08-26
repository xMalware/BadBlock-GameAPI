package fr.badblock.game.core18R3.listeners;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.merchant.shops.Merchant;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.configuration.values.MapList;
import fr.badblock.gameapi.configuration.values.MapRecipe;
import fr.badblock.gameapi.events.PlayerFakeEntityInteractEvent;
import fr.badblock.gameapi.packets.in.play.PlayInUseEntity.UseEntityAction;
import fr.badblock.gameapi.utils.merchants.CustomMerchantRecipe;

public class ShopListener extends BadListener {
	public static Map<UUID, Merchant> inEdit = Maps.newConcurrentMap();
	
	@EventHandler(ignoreCancelled=true)
	public void onFakeInteract(PlayerFakeEntityInteractEvent e){
		if(e.getAction() == UseEntityAction.INTERACT){

			for(Merchant merchant : GamePlugin.getInstance().getMerchants().values()){

				if(merchant.click(e.getPlayer(), e.getEntity())){
					break;
				}

			}

		}

	}

	/*
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		for(Merchant merchant : GamePlugin.getInstance().getMerchants().values()){
			merchant.move((BadblockPlayer) e.getPlayer(), e.getPlayer().getLocation().clone().add(0, 64, 0), e.getPlayer().getLocation());
		}
	}*/
	
	/*@EventHandler
	public void onMove(PlayerMoveEvent e){
		if(e.getTo().getBlock().equals(e.getFrom().getBlock())) return;
		
		for(Merchant merchant : GamePlugin.getInstance().getMerchants().values()){
			merchant.move((BadblockPlayer) e.getPlayer(), e.getFrom(), e.getTo());
		}
	}*/
	
	/*@EventHandler
	public void onMove(PlayerTeleportEvent e){
		for(Merchant merchant : GamePlugin.getInstance().getMerchants().values()){
			merchant.move((BadblockPlayer) e.getPlayer(), e.getFrom(), e.getTo());
		}
	}*/
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e){
		Merchant merchant = null;
		
		if(!inEdit.containsKey(e.getPlayer().getUniqueId())){
			return;
		} else merchant = inEdit.remove(e.getPlayer().getUniqueId());
		
		MapList<MapRecipe, CustomMerchantRecipe> list = new MapList<>();
		
		for(int i=0;i<9;i++){
			ItemStack first  = e.getInventory().getItem(i);
			ItemStack second = e.getInventory().getItem(i + 9);
			ItemStack result = e.getInventory().getItem(i + 27);
			
			if(first == null) continue;
			
			list.add(new MapRecipe(new CustomMerchantRecipe(first, second, result)));
		}
		
		merchant.setRecipes(list);
		merchant.save().save(file(merchant));
	}
	
	private File file(Merchant merchant){
		return new File(GamePlugin.getInstance().getShopFolder(), merchant.getName().toLowerCase() + ".json");
	}
}

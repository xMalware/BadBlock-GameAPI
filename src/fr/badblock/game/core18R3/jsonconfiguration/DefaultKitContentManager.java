package fr.badblock.game.core18R3.jsonconfiguration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.configuration.BadConfiguration;
import fr.badblock.gameapi.configuration.values.MapItemStack;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.kits.PlayerKitContentManager;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;

public class DefaultKitContentManager implements PlayerKitContentManager {
	private boolean allowDrop = false;

	public DefaultKitContentManager(boolean allowDrop) {
		this.allowDrop = allowDrop;
	}

	@Override
	public void give(JsonObject content, BadblockPlayer player) {
		BadConfiguration configuration = GameAPI.getAPI().loadConfiguration(content);

		player.clearInventory();

		player.getInventory().setContents(configuration.getValueList("content", MapItemStack.class).getHandle().toArray(new ItemStack[0]));
		player.getInventory().setArmorContents(configuration.getValueList("armor", MapItemStack.class).getHandle().toArray(new ItemStack[0]));

		for(ItemStack is : player.getInventory().getContents()){
			prepareItem(player, is, allowDrop);
		}


		for(ItemStack is : player.getInventory().getArmorContents()){
			prepareItem(player, is, allowDrop);
		}

		player.updateInventory();
	}
	
	@Override
	public JsonObject createFromInventory(BadblockPlayer player) {
		BadConfiguration configuration = GameAPI.getAPI().loadConfiguration(new JsonObject());

		configuration.setValueList("content", MapItemStack.toMapList(items(player.getInventory().getContents())));
		configuration.setValueList("armor", MapItemStack.toMapList(items(player.getInventory().getArmorContents())));

		return configuration.save();
	}
	
	private List<ItemStack> items(ItemStack[] items){
		List<ItemStack> result = new ArrayList<>();
		
		for(ItemStack item : items){
			if(ItemStackUtils.isValid(item))
				removeEmptyLore(item);
			
			result.add(item);
		}
		
		return result;
	}
	
	private void removeEmptyLore(ItemStack is){
		List<String> lore = is.getItemMeta().getLore();
		List<String> res  = new ArrayList<>();
		
		if(lore != null && !lore.isEmpty()){
			for(String l : lore){
				if(!l.replaceAll("§.", "").isEmpty())
					res.add(l);
			}
			
			
			ItemStackUtils.changeLore(is, res);
		}
	}
	
	private void prepareItem(BadblockPlayer player, ItemStack is, boolean allowDrop){
		if(!ItemStackUtils.isValid(is)) return;

		removeEmptyLore(is);
		
		if(player.getTeam() != null && is.getItemMeta() instanceof LeatherArmorMeta){
			LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
			
			meta.setColor(player.getTeam().geNormalColor());
			is.setItemMeta(meta);
		}
		
		if(is.getType().getMaxStackSize() > 1 || allowDrop) return; // que stuff quoi
		
		GameAPI.getAPI().createItemStackExtra(is).disallow(ItemAction.DROP)
						.disallow(ItemAction.INVENTORY_DROP)
						.allowDropOnDeath(false);
		
		return;
	}
}

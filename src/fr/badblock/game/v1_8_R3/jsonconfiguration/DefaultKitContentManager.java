package fr.badblock.game.v1_8_R3.jsonconfiguration;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.configuration.BadConfiguration;
import fr.badblock.gameapi.configuration.values.MapItemStack;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.kits.PlayerKitContentManager;

public class DefaultKitContentManager implements PlayerKitContentManager {
	@Override
	public void give(JsonObject content, BadblockPlayer player) {
		BadConfiguration configuration = GameAPI.getAPI().loadConfiguration(content);
		
		player.getInventory().setContents(to(configuration.getValueList("content", MapItemStack.class)));
		player.getInventory().setArmorContents(to(configuration.getValueList("armor", MapItemStack.class)));
	}
	
	private ItemStack[] to(List<MapItemStack> list){
		ItemStack[] is = new ItemStack[list.size()];
		
		int i = 0;
		for(MapItemStack mis : list){
			is[i] = mis.getHandle();
			i++;
		}
		
		return is;
	}
}

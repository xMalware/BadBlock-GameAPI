package fr.badblock.game.v1_8_R3.jsonconfiguration;

import java.util.Arrays;

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
		
		player.clearInventory();
		
		player.getInventory().setContents(configuration.getValueList("content", MapItemStack.class).getHandle().toArray(new ItemStack[0]));
		player.getInventory().setArmorContents(configuration.getValueList("armor", MapItemStack.class).getHandle().toArray(new ItemStack[0]));
	
		player.updateInventory();
	}
	
	@Override
	public JsonObject createFromInventory(BadblockPlayer player) {
		BadConfiguration configuration = GameAPI.getAPI().loadConfiguration(new JsonObject());
		
		configuration.setValueList("content", MapItemStack.toMapList(Arrays.asList(player.getInventory().getContents())));
		configuration.setValueList("armor", MapItemStack.toMapList(Arrays.asList(player.getInventory().getArmorContents())));
		
		return configuration.save();
	}
}

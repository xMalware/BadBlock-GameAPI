package fr.badblock.game.v1_8_R3.jsonconfiguration;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.badblock.gameapi.utils.JsonConfiguration;

public class GameJsonConfiguration implements JsonConfiguration {

	@Override
	public JsonObject saveItemStack(ItemStack item) {
		return new Gson().toJsonTree(new JsonItemStack(item)).getAsJsonObject();
	}

	@Override
	public ItemStack loadItemStack(JsonObject object) {
		return new Gson().fromJson(object, JsonItemStack.class).get();
	}
	
	@Override
	public ItemStack[] loadItemArray(JsonArray array) {
		ItemStack[] items = new ItemStack[array.size()];
		
		for(int i=0;i<array.size();i++){
			items[i] = loadItemStack(array.get(i).getAsJsonObject());
		}
		
		return items;
	}

	@Override
	public JsonObject saveLocation(Location location) {
		return new Gson().toJsonTree(new JsonLocation(location)).getAsJsonObject();
	}

	@Override
	public Location loadLocation(JsonObject object) {
		return new Gson().fromJson(object, JsonLocation.class).get();
	}

}

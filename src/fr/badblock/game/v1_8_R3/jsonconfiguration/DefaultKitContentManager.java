package fr.badblock.game.v1_8_R3.jsonconfiguration;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.kits.PlayerKitContentManager;

public class DefaultKitContentManager implements PlayerKitContentManager {
	@Override
	public void give(JsonObject content, BadblockPlayer player) {
		player.getInventory().setContents(GameAPI.getAPI().getJsonConfiguration().loadItemArray(content.get("content").getAsJsonArray()));
		player.getInventory().setArmorContents(GameAPI.getAPI().getJsonConfiguration().loadItemArray(content.get("armor").getAsJsonArray()));
	}
}

package fr.badblock.bukkit.hub.inventories.selector.googleauth;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.warrenstrange.googleauth.GoogleAuthenticator;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.general.Callback;

public class AuthUtils {

	public static GoogleAuthenticator gAuth 			= new GoogleAuthenticator();
	public static Map<String, String> tempPlayersKeys   = new HashMap<>();
	
	// Same code in BadAuthenticate
	public static void getAuthKey(String player, Callback<String> callback) {
		getProfile(player, new Callback<JsonObject>() {

			@Override
			public void done(JsonObject result, Throwable error) {
				if (!result.has("authKey")) {
					callback.done(null, null);
					return;
				}
				callback.done(result.get("authKey").getAsString(), null);
			}

		});
	}
	
	public static void getProfile(String player, Callback<JsonObject> json) {
		GameAPI.getAPI().getLadderDatabase().getPlayerData(player, json);
	}
	
}

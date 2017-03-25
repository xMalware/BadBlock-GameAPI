package fr.badblock.bukkit.hub.tasks;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.boosters.inventories.RealTimeBoosterManager;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.utils.general.Callback;

public class RequestBoosterTask extends CustomTask {

	private static final Type type = new TypeToken<Map<String, PlayerBooster>>() {}.getType();
	public static boolean work;

	public RequestBoosterTask() {
		super(0, 20 * 300);
	}

	@Override
	public void done() {
		work();
	}
	
	public static void work() {
		GameAPI.getAPI().getSqlDatabase().call("SELECT value FROM keyValues WHERE `key` = 'booster'", SQLRequestType.QUERY, new Callback<ResultSet>() {

			@Override
			public void done(ResultSet result, Throwable error) {
				try {
					result.next();
					String value = result.getString("value");
					BadBlockHub hub = BadBlockHub.getInstance();
					Gson gson = hub.getGson();
					Map<String, PlayerBooster> updatedMap = gson.fromJson(value, type);
					RealTimeBoosterManager.stockage = updatedMap;
					work = true;
					result.close(); // don't forget to close it
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		});
	}

}

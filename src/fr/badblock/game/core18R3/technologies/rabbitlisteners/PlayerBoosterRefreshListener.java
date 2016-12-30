package fr.badblock.game.core18R3.technologies.rabbitlisteners;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class PlayerBoosterRefreshListener extends RabbitListener {

	public PlayerBoosterRefreshListener() {
		super(RabbitConnector.getInstance().getService("default"), "boosterRefresh", true, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		if (Bukkit.getServerName().startsWith(body)) {
			if (GameAPI.getAPI().getRunType().equals(RunType.GAME)) {
				GameAPI.getAPI().getSqlDatabase().call("SELECT value FROM keyValues WHERE `key` = 'booster'", SQLRequestType.QUERY, new Callback<ResultSet>() {

					@Override
					public void done(ResultSet result, Throwable error) {
						try {
							result.next();
							String value = result.getString("value");
							Map<String, PlayerBooster> updatedMap = GamePlugin.getGson().fromJson(value, GamePlugin.type);
							for (Entry<String, PlayerBooster> entry : updatedMap.entrySet()) {
								if (Bukkit.getServerName().startsWith(entry.getKey())) {
									GamePlugin.getInstance().gamePrefix = entry.getKey();
									GamePlugin.getInstance().booster = entry.getValue();
									break;
								}
							}
							result.close(); // don't forget to close it
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}

				});
			}
		}
	}

}

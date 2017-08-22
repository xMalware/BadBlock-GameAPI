package fr.badblock.game.core18R3.technologies.rabbitlisteners;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class PlayerPingListener extends RabbitListener {

	public static Map<String, Integer> ping = new HashMap<>();
	private static Gson gson = new Gson();
	public static final Type collectionType = new TypeToken<Map<String, Integer>>() {}.getType();

	public PlayerPingListener() {
		super(RabbitConnector.getInstance().getService("default"), "playerPing", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		Map<String, Integer> temp = gson.fromJson(body, collectionType);
		for (Entry<String, Integer> entry : temp.entrySet()) {
			ping.put(entry.getKey(), entry.getValue());
		}
	}

}

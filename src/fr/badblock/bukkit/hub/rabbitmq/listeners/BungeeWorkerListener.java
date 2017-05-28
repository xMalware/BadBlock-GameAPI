package fr.badblock.bukkit.hub.rabbitmq.listeners;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class BungeeWorkerListener extends RabbitListener {
	
	public static double bungeeWorkers = 1; // un seul bungee qui work et qui te fetch dessus
	
	public BungeeWorkerListener() {
		super(BadBlockHub.getInstance().getRabbitService(), "ladder.bungeeWorkerHub", false, RabbitListenerType.SUBSCRIBER);
	}
	
	@Override
	public void onPacketReceiving(String body) {
		bungeeWorkers = Double.parseDouble(body);
	}
	
}


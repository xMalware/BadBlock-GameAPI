package fr.badblock.bukkit.hub.rabbitmq;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class QuitListener extends RabbitListener {
	
	public QuitListener() {
		super(BadBlockHub.getInstance().getRabbitService(), "quit", false, RabbitListenerType.SUBSCRIBER);
	}
	
	@Override
	public void onPacketReceiving(String body) {
		if (SEntryInfosListener.tempPlayers.containsKey(body)) {
			SEntryInfosListener.tempPlayers.put(body, 0L);
		}
	}
	
}


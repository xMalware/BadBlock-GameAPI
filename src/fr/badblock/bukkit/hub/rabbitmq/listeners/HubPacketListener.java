package fr.badblock.bukkit.hub.rabbitmq.listeners;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.rabbitmq.Hub;
import fr.badblock.bukkit.hub.rabbitmq.factories.HubAliveFactory;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class HubPacketListener extends RabbitListener {

	public HubPacketListener() {
		super(BadBlockHub.getInstance().getRabbitService(), "hub", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		if (body == null)
			return;
		HubAliveFactory hubAliveFactory = BadBlockHub.getInstance().getGson().fromJson(body, HubAliveFactory.class);
		if (hubAliveFactory == null)
			return;
		Hub hub = Hub.get(hubAliveFactory.getId());
		if (hub == null) {
			hub = new Hub(hubAliveFactory.getId(), hubAliveFactory.getName());
			hub.create();
		}
		hub.keepAlive(hubAliveFactory.getPlayers(), hubAliveFactory.getSlots(), hubAliveFactory.isOpened(),
				hubAliveFactory.getRanks());
	}

}

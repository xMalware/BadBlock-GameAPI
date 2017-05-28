package fr.badblock.bukkit.hub.rabbitmq.listeners;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.selector.dev.DevSelectorInventory;
import fr.badblock.bukkit.hub.rabbitmq.factories.DevAliveFactory;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class DevPacketListener extends RabbitListener {
	public DevPacketListener() {
		super(BadBlockHub.getInstance().getRabbitService(), "dev", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		if (body == null)
			return;
		
		DevAliveFactory devAliveFactory = BadBlockHub.getInstance().getGson().fromJson(body, DevAliveFactory.class);
		
		if (devAliveFactory == null)
			return;
		DevSelectorInventory.Apply(devAliveFactory);
	}
}

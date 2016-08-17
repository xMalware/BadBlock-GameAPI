package fr.badblock.game.core18R3.technologies;

import java.io.IOException;

import fr.badblock.game.core18R3.jsonconfiguration.APIConfig;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.types.RabbitPacketType;
import fr.badblock.rabbitconnector.workers.RabbitService;
import fr.badblock.utils.Encodage;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class RabbitSpeaker implements fr.badblock.gameapi.technologies.RabbitSpeaker {
	
	@Getter @Setter private static RabbitConnector		rabbitConnector		= RabbitConnector.getInstance();;
	
	private	RabbitService								rabbitService;

	public RabbitSpeaker(APIConfig apiConfig) throws IOException {
		this.setRabbitService(getRabbitConnector().newService("default", apiConfig.getRabbitHostname(), apiConfig.getRabbitPort(), apiConfig.getRabbitUsername(), apiConfig.getRabbitPassword(), apiConfig.getRabbitVirtualHost()));
	}

	@Override
	public void sendAsyncUTF8Message(String queueName, String content, long ttl, boolean debug) {
		this.getRabbitService().sendPacket(queueName, content, Encodage.UTF8, RabbitPacketType.MESSAGE_BROKER, ttl, debug);
	}

	@Override
	public void sendAsyncUTF8Publisher(String queueName, String content, long ttl, boolean debug) {
		this.getRabbitService().sendPacket(queueName, content, Encodage.UTF8, RabbitPacketType.PUBLISHER, ttl, debug);
	}

	@Override
	public void sendSyncUTF8Message(String queueName, String content, long ttl, boolean debug) {
		this.getRabbitService().sendSyncPacket(queueName, content, Encodage.UTF8, RabbitPacketType.MESSAGE_BROKER, ttl, debug);
	}

	@Override
	public void sendSyncUTF8Publisher(String queueName, String content, long ttl, boolean debug) {
		this.getRabbitService().sendSyncPacket(queueName, content, Encodage.UTF8, RabbitPacketType.PUBLISHER, ttl, debug);
	}
	
}

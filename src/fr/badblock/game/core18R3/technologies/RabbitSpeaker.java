package fr.badblock.game.core18R3.technologies;

import java.io.IOException;

import fr.badblock.game.core18R3.jsonconfiguration.data.RabbitMQConfig;
import fr.badblock.rabbitconnector.RabbitConnector;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.rabbitconnector.RabbitService;
import fr.badblock.utils.Encodage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitSpeaker implements fr.badblock.gameapi.technologies.RabbitSpeaker {

	@Getter
	@Setter
	private static RabbitConnector rabbitConnector = RabbitConnector.getInstance();;

	private RabbitService rabbitService;

	public RabbitSpeaker(RabbitMQConfig rabbitMQConfig) throws IOException {
		this.setRabbitService(getRabbitConnector().newService("default", rabbitMQConfig.getRabbitPort(), rabbitMQConfig.getRabbitUsername(), rabbitMQConfig.getRabbitPassword(), rabbitMQConfig.getRabbitVirtualHost(), rabbitMQConfig.getRabbitHostname()));
	}

	@Override
	public void sendAsyncUTF8Message(String queueName, String content, long ttl, boolean debug) {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		this.getRabbitService().sendAsyncPacket(queueName, content, Encodage.UTF8, RabbitPacketType.MESSAGE_BROKER, ttl, debug);
	}

	@Override
	public void sendAsyncUTF8Publisher(String queueName, String content, long ttl, boolean debug) {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		this.getRabbitService().sendAsyncPacket(queueName, content, Encodage.UTF8, RabbitPacketType.PUBLISHER, ttl, debug);
	}

	@Override
	public void sendSyncUTF8Message(String queueName, String content, long ttl, boolean debug) {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		this.getRabbitService().sendSyncPacket(queueName, content, Encodage.UTF8, RabbitPacketType.MESSAGE_BROKER, ttl, debug);
	}

	@Override
	public void sendSyncUTF8Publisher(String queueName, String content, long ttl, boolean debug) {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		this.getRabbitService().sendSyncPacket(queueName, content, Encodage.UTF8, RabbitPacketType.PUBLISHER, ttl, debug);
	}

	@Override
	public void cut() {
		System.getSecurityManager().checkPermission(new RuntimePermission("badblockDatabase"));

		try {
			this.getRabbitService().getChannel().close();
			this.getRabbitService().getConnection().close();
		}catch(Exception error) {
			error.printStackTrace();
		}
	}

}

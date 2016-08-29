package fr.badblock.game.core18R3.jsonconfiguration.data;

import fr.badblock.gameapi.run.RunType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitMQConfig {

	// Rabbit part
	public String rabbitHostname;
	public int rabbitPort;
	public String rabbitUsername;
	public String rabbitPassword;
	public String rabbitVirtualHost;

}
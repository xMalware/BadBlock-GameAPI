package fr.badblock.game.v1_8_R3.game;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import com.google.gson.Gson;

import fr.badblock.common.docker.factories.ServerConfigurationFactory;
import fr.badblock.game.v1_8_R3.game.threading.GameServerKeeperAliveTask;
import fr.badblock.game.v1_8_R3.game.threading.GameServerMonitoringTask;
import fr.badblock.game.v1_8_R3.game.threading.GameServerSendLogsTask;
import fr.badblock.game.v1_8_R3.jsonconfiguration.APIConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.general.JsonUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * GameServer Manager (this system know where and when send any information about the currently instance)
 * @author xMalware
 */
@Getter@Setter
public class GameServerManager {
	private	Gson						gson;
	private APIConfig					apiConfig;
	private ConsoleCommandSender		console;
	private GameServerSendLogsTask		gameServerSendLogsTask;
	private GameServerKeeperAliveTask	gameServerKeeperAliveTask;
	private GameServerMonitoringTask	gameServerMonitoringTask;
	private boolean						loaded;
	//private ServerConfigurationFactory	serverConfigurationFactory;

	public GameServerManager(APIConfig config) {
		this.setApiConfig(config);
		this.setGson(GameAPI.getPrettyGson());
		this.setConsole(Bukkit.getConsoleSender());
	}
	
	public void start() {
		// Activation du timing
		forceCommand("timings on");

		if (!GameAPI.TEST_MODE) {
			File gameServerFile  = new File(GameServerUtils.getFileName());
			if(!gameServerFile.exists()) {
				GameServerMessages.GAMESERVER_FILE_NOT_FOUND.log();
				GameServerMessages.SENDING_SHUTDOWN_SIGNAL.log();
				Bukkit.shutdown();
				return;
			}
		/*	this.setServerConfigurationFactory(JsonUtils.load(gameServerFile, ServerConfigurationFactory.class));
			if(this.getServerConfigurationFactory() == null) {
				GameServerMessages.GAMESERVER_FILE_INVALID.log();
				GameServerMessages.SENDING_SHUTDOWN_SIGNAL.log();
				Bukkit.shutdown();
				return;
			}*/
			this.setGameServerKeeperAliveTask(new GameServerKeeperAliveTask(this.getApiConfig()));
			this.setGameServerMonitoringTask(new GameServerMonitoringTask(this.getApiConfig()));
			this.setGameServerSendLogsTask(new GameServerSendLogsTask(this.getApiConfig()));

			this.setLoaded(true);
		}
	}

	public void stop() {
		GameServerMessages.UNLOADING.log();

		this.forceCommand("timings paste");
		if (this.isLoaded() && !GameAPI.TEST_MODE) {
			getGameServerSendLogsTask().doLog();
			this.getGameServerKeeperAliveTask().sendStopPacket();
		}

		GameServerMessages.UNLOADED.log();
	}
	
	private void forceCommand(String message) {
		Bukkit.dispatchCommand(console, message);
	}

}
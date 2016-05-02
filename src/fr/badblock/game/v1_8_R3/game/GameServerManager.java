package fr.badblock.game.v1_8_R3.game;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import com.google.gson.Gson;

import fr.badblock.common.docker.factories.ServerConfigurationFactory;
import fr.badblock.game.v1_8_R3.game.threading.GameServerKeeperAliveTask;
import fr.badblock.game.v1_8_R3.game.threading.GameServerMonitoringTask;
import fr.badblock.game.v1_8_R3.game.threading.GameServerSendLogsThread;
import fr.badblock.game.v1_8_R3.jsonconfiguration.APIConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.game.v1_8_R3.game.GameServerMessages.*;
import lombok.Getter;
import lombok.Setter;

/**
 * GameServer Manager (this system know where and when send any information about the currently instance)
 */
@Getter@Setter
public class GameServerManager {

	private	Gson						gson;
	private APIConfig					apiConfig;
	private	String						version						= "0.1";
	private ConsoleCommandSender		console;
	private String						serverName;
	private String						logsFile;
	private long						joinTime;
	private GameServerSendLogsThread	gameServerSendLogsThread;
	private boolean						loaded;
	private ServerConfigurationFactory	serverConfigurationFactory;

	public GameServerManager(APIConfig config) {
		this.setApiConfig(apiConfig);
		this.setGson(JsonUtils.getGson());
		this.setConsole(Bukkit.getConsoleSender());

		// Activation du timing
		forceCommand("timings on");

		if(!GameAPI.TEST_MODE) {
			File gameServerFile  = new File(GameServerUtils.getFileName());
			if(!gameServerFile.exists()) {
				GameServerMessages.GAMESERVER_FILE_NOT_FOUND.log();
				GameServerMessages.SENDING_SHUTDOWN_SIGNAL.log();
				Bukkit.shutdown();
				return;
			}
			this.setServerConfigurationFactory(JsonUtils.load(gameServerFile, ServerConfigurationFactory.class));
			if(this.getServerConfigurationFactory() == null) {
				GameServerMessages.GAMESERVER_FILE_INVALID.log();
				GameServerMessages.SENDING_SHUTDOWN_SIGNAL.log();
				Bukkit.shutdown();
				return;
			}
			this.incrementJoinTime();

			new GameServerKeeperAliveTask(config);
			new GameServerMonitoringTask(config);
			new GameServerSendLogsThread(config);

			this.setLoaded(true);
		}
	}

	public void stop() {
		GameServerMessages.UNLOADING.log();

		this.forceCommand("timings paste");
		if (this.isLoaded()) {
			if(!GameAPI.TEST_MODE)
				getGameServerSendLogsThread().doLog();
			keepAlive(GameState.STOPPING);
		}

		GameServerMessages.UNLOADED.log();
	}

	public void incrementJoinTime() {
		this.setJoinTime(System.currentTimeMillis() + this.getApiConfig().uselessUntilTime);
	}

	public void keepAlive() {
		keepAlive(GameAPI.getAPI().getGameServer().getGameState());
	}

	public void keepAlive(GameState status) {
		if(!GameAPI.TEST_MODE)
			GameAPI.getAPI().getLadderDatabase().keepAlive(status, Bukkit.getOnlinePlayers().size(), GameAPI.getAPI().getGameServer().getMaxPlayers());
	}

	private void forceCommand(String message) {
		Bukkit.dispatchCommand(console, message);
	}

}
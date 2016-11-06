package fr.badblock.game.core18R3.gameserver;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import com.google.gson.Gson;

import fr.badblock.game.core18R3.gameserver.threading.GameServerKeeperAliveTask;
import fr.badblock.game.core18R3.gameserver.threading.GameServerMonitoringTask;
import fr.badblock.game.core18R3.gameserver.threading.GameServerSendLogsTask;
import fr.badblock.game.core18R3.jsonconfiguration.data.FTPConfig;
import fr.badblock.game.core18R3.jsonconfiguration.data.GameServerConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.threading.TaskManager;
import lombok.Getter;
import lombok.Setter;

/**
 * GameServer Manager (this system know where and when send any information
 * about the currently instance)
 * 
 * @author xMalware
 */
@Getter
@Setter
public class GameServerManager {
	
	private Gson gson;
	private GameServerConfig gameServerConfig;
	private FTPConfig ftpConfig;
	private ConsoleCommandSender console;
	private GameServerSendLogsTask gameServerSendLogsTask;
	private GameServerKeeperAliveTask gameServerKeeperAliveTask;
	private GameServerMonitoringTask gameServerMonitoringTask;
	private boolean loaded;
	// private ServerConfigurationFactory serverConfigurationFactory;

	public GameServerManager(GameServerConfig config, FTPConfig ftpConfig) {
		this.setGameServerConfig(config);
		this.setFtpConfig(ftpConfig);
		this.setGson(GameAPI.getPrettyGson());
		this.setConsole(Bukkit.getConsoleSender());
	}

	private void forceCommand(String message) {
		Bukkit.dispatchCommand(console, message);
	}

	public void start() {
		// Activation du timing
		forceCommand("timings off");

		if (!GameAPI.TEST_MODE) {
			File gameServerFile = new File(GameServerUtils.getFileName());
			if (!gameServerFile.exists()) {
				GameServerMessages.GAMESERVER_FILE_NOT_FOUND.log();
				GameServerMessages.SENDING_SHUTDOWN_SIGNAL.log();
				Bukkit.shutdown();
				return;
			}
			/*
			 * this.setServerConfigurationFactory(JsonUtils.load(gameServerFile,
			 * ServerConfigurationFactory.class));
			 * if(this.getServerConfigurationFactory() == null) {
			 * GameServerMessages.GAMESERVER_FILE_INVALID.log();
			 * GameServerMessages.SENDING_SHUTDOWN_SIGNAL.log();
			 * Bukkit.shutdown(); return; }
			 */
			this.setGameServerKeeperAliveTask(new GameServerKeeperAliveTask(this.getGameServerConfig()));
			this.setGameServerMonitoringTask(new GameServerMonitoringTask(this.getGameServerConfig()));
			this.setGameServerSendLogsTask(new GameServerSendLogsTask(this.getGameServerConfig(), this.getFtpConfig()));

			this.setLoaded(true);
		}
		TaskManager.runTaskLater(new Runnable() {

			@Override
			public void run() {
				forceCommand("timings off");
			}
			
		}, 20);
	}

	public void stop() {
		GameServerMessages.UNLOADING.log();

		//this.forceCommand("timings paste");
		if (this.isLoaded() && !GameAPI.TEST_MODE) {
			getGameServerSendLogsTask().doLog();
			this.getGameServerKeeperAliveTask().sendStopPacket();
		}

		GameServerMessages.UNLOADED.log();
	}

}
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

import fr.badblock.game.v1_8_R3.game.threading.GameServerKeeperAliveTask;
import fr.badblock.game.v1_8_R3.game.threading.GameServerMonitoringTask;
import fr.badblock.game.v1_8_R3.game.threading.GameServerSendLogsThread;
import fr.badblock.game.v1_8_R3.jsonconfiguration.APIConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.GameState;
import lombok.Getter;
import lombok.Setter;

/**
 * Manager du GameServer (sait où et quand envoyer les infos de l'instance)
 */
@Getter@Setter
public class GameServerManager {
	private	Gson						gson;
	private	String						version				= "0.1";
	private boolean						isTheFirstServer	= true;
	private ConsoleCommandSender		console;
	private String						serverName;
	private String						logsFile;
	private long						joinTime;
	private GameServerSendLogsThread	gameServerSendLogsThread;

	public GameServerManager(APIConfig config) {
		this.setGson(new Gson());

		this.setConsole(Bukkit.getConsoleSender());

		// Activation du timing

		Bukkit.dispatchCommand(console, "timings on");

		if(!GameAPI.TEST_MODE){
			this.incrementJoinTime();
			this.loadConfigurationFile(config);
			this.setFirstServer();

			new GameServerKeeperAliveTask();
			new GameServerMonitoringTask();
			new GameServerSendLogsThread(config);
		}
	}

	public void stop() {
		GameAPI.logColor("&b[GameServer] &eUnloading...");

		Bukkit.dispatchCommand(this.getConsole(), "timings paste");
		if(!GameAPI.TEST_MODE)
			getGameServerSendLogsThread().doLog();
		keepAlive(GameState.STOPING);

		GameAPI.logColor("&b[GameServer] &aUnloaded!");
	}

	public void incrementJoinTime() {
		this.setJoinTime(System.currentTimeMillis() + 1800_000L);
	}

	private void setFirstServer() {
		String string = logsFile.split("/")[0];
		int i = Integer.parseInt(Bukkit.getServerName().replace(string, ""));

		OptionalInt optionalInt = Arrays.stream(new File("..").listFiles()).mapToInt((file) -> {
			if(file.isDirectory())
				try {
					return Integer.parseInt(file.getName());
				} catch(Exception unused){}
			return -1;
		}).filter((value) ->  value > 0).min();

		this.setTheFirstServer(optionalInt.isPresent() && optionalInt.getAsInt() == i);
	}

	private void loadConfigurationFile(APIConfig config) {
		try {
			List<String> lines = Files.readAllLines(Paths.get("server.properties"), Charset.defaultCharset());
			long time = System.currentTimeMillis() - Long.parseLong(get(lines, "docker-started", "" + System.currentTimeMillis()));
			GameAPI.logColor("&b[GameServer] &6Loaded instance in " + time / 1000D + " seconds.");
			GameAPI.logColor("&b[GameServer] &6Version: &e" + version + "&6.");
			String mode = get(lines, "docker-mode", "rush");
			GameAPI.logColor("&b[GameServer] &eChoosed &6" + mode + " &emode.");
			String map = get(lines, "docker-map", "rush2v2");
			GameAPI.logColor("&b[GameServer] &eWorld: &6" + map + "&e.");
			serverName = get(lines, "server-name", "rush2v21");
			GameAPI.logColor("&b[GameServer] &eServer name: &6" + serverName + "&e.");
			String prefix = get(lines, "docker-prefix", "[Docker]");
			GameAPI.logColor("&b[GameServer] &eDocker prefix: &6" + prefix + "&e.");
			logsFile = get(lines, "docker-logs", "rush2v2/yolo");
			GameAPI.logColor("&b[GameServer] &eLogs file: &6" + logsFile + "&e.");
			GameAPI.logColor("&b[GameServer] &aServer is working with Docker for BadBlock by xMalware.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String get(List<String> lines, String name, String def) {
		for(String line : lines) {
			String[] splitter = line.split("=");
			if (splitter[0].equals(name)) return splitter[1];
		}

		return def;
	}

	public void keepAlive(){
		keepAlive(GameAPI.getAPI().getGameServer().getGameState());
	}

	public void keepAlive(GameState status) {
		if(!GameAPI.TEST_MODE)
			GameAPI.getAPI().getLadderDatabase().keepAlive(status, Bukkit.getOnlinePlayers().size(), GameAPI.getAPI().getGameServer().getMaxPlayers());
	}
}
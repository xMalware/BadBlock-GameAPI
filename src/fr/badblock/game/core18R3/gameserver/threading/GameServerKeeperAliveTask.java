package fr.badblock.game.core18R3.gameserver.threading;

import java.io.File;
import java.util.Arrays;
import java.util.OptionalInt;

import org.bukkit.Bukkit;

import fr.badblock.docker.factories.GameAliveFactory;
import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.gameserver.GameServerManager;
import fr.badblock.game.core18R3.jsonconfiguration.data.GameServerConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.GameServer;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameServerKeeperAliveTask extends GameServerTask {

	private boolean firstServer;
	private long joinTime;

	public GameServerKeeperAliveTask(GameServerConfig apiConfig) {
		this.incrementJoinTime();
		TaskManager.scheduleAsyncRepeatingTask("gameServerKeeperAlive", this, 0, apiConfig.ticksBetweenKeepAlives);
	}

	public void incrementJoinTime() {
		this.setJoinTime(System.currentTimeMillis() + this.getGameServerManager().getGameServerConfig().uselessUntilTime);
	}

	private boolean isJoinable() {
		GameState gameState = GamePlugin.getInstance().getGameServer().getGameState();
		return gameState.equals(GameState.WAITING) || (gameState.equals(GameState.RUNNING) && GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && BukkitUtils.getPlayers().size() < Bukkit.getMaxPlayers());
	}

	public void keepAlive(int addedPlayers) {
		keepAlive(GameAPI.getAPI().getGameServer().getGameState(), addedPlayers);
	}

	public void keepAlive(GameState gameState, int addedPlayers) {
		if (!GameAPI.TEST_MODE)
			sendKeepAlivePacket(gameState, addedPlayers);
	}

	@Override
	public void run() {
		this.setFirstServer();
		if (getJoinTime() < System.currentTimeMillis() && Bukkit.getOnlinePlayers().size() == 0
				&& !this.isFirstServer()) {
			GameAPI.logColor("&b[GameServer] &cNobody during few minutes, shutdown..");
			Bukkit.shutdown();
			return;
		}
		this.keepAlive(0);
	}

	private void sendKeepAlivePacket(GameState gameState, int addedPlayers) {
		GameAPI gameApi = GameAPI.getAPI();
		GameServerManager gameServerManager = this.getGameServerManager();
		// ServerConfigurationFactory serverConfigurationFactory =
		// gameServerManager.getServerConfigurationFactory();
		GameServer gameServer = gameApi.getGameServer();
		GameAliveFactory gameAliveFactory = new GameAliveFactory(gameApi.getServer().getServerName(), fr.badblock.docker.GameState.getStatus(gameServer.getGameState().getId()), isJoinable(), Bukkit.getOnlinePlayers().size() + addedPlayers, gameServer.getMaxPlayers());
		gameApi.getRabbitSpeaker().sendAsyncUTF8Publisher("networkdocker.instance.keepalive", gameServerManager.getGson().toJson(gameAliveFactory), 5000, false);
	}

	public void sendStopPacket() {
		GameAPI gameApi = GameAPI.getAPI();
		// ServerConfigurationFactory serverConfigurationFactory =
		// gameServerManager.getServerConfigurationFactory();
		gameApi.getRabbitSpeaker().sendSyncUTF8Publisher("networkdocker.instance.stop", gameApi.getServer().getServerName(), 5000, false);
		gameApi.getRabbitSpeaker().cut();
	}

	public void setFirstServer() {
		String[] split = GameAPI.getAPI().getServer().getServerName().split("_");
		if (split.length < 2)
			return;
		String af = split[1];
		long serverId = Integer.parseInt(af);
		OptionalInt optionalInt = Arrays.stream(new File("..").listFiles()).mapToInt((file) -> {
			if (file.isDirectory())
				try {
					return Integer.parseInt(file.getName());
				} catch (Exception unused) {
				}
			return -1;
		}).filter((value) -> value > 0).min();

		this.setFirstServer(optionalInt.isPresent() && optionalInt.getAsInt() == serverId);
	}

}

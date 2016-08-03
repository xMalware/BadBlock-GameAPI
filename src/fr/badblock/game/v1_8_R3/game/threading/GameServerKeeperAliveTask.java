package fr.badblock.game.v1_8_R3.game.threading;

import java.io.File;
import java.util.Arrays;
import java.util.OptionalInt;

import org.bukkit.Bukkit;

import fr.badblock.common.docker.factories.GameAliveFactory;
import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.game.v1_8_R3.game.GameServerManager;
import fr.badblock.game.v1_8_R3.jsonconfiguration.APIConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.GameServer;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.utils.threading.TaskManager;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class GameServerKeeperAliveTask extends GameServerTask {
	
	private boolean	firstServer;
	private long	joinTime;
	
	public GameServerKeeperAliveTask(APIConfig apiConfig) {
		this.incrementJoinTime();
		TaskManager.scheduleSyncRepeatingTask("gameServerKeeperAlive", this, 0, apiConfig.ticksBetweenKeepAlives);
	}

	@Override
	public void run() {
		this.setFirstServer();
		if (getJoinTime() < System.currentTimeMillis() && Bukkit.getOnlinePlayers().size() == 0 && !this.isFirstServer()) {
			GameAPI.logColor("&b[GameServer] &cNobody during a part of 30 minutes, shutdown...");
			Bukkit.shutdown();
			return;
		}
		this.keepAlive();
	}	
	
	public void incrementJoinTime() {
		this.setJoinTime(System.currentTimeMillis() + this.getGameServerManager().getApiConfig().uselessUntilTime);
	}

	public void keepAlive() {
		keepAlive(GameAPI.getAPI().getGameServer().getGameState());
	}

	public void keepAlive(GameState status) {
		if(!GameAPI.TEST_MODE)
			sendKeepAlivePacket(status);
	}
	
	private void sendKeepAlivePacket(GameState gameState) {
		GameAPI gameApi = GameAPI.getAPI();
		GameServerManager gameServerManager = this.getGameServerManager();
		//ServerConfigurationFactory serverConfigurationFactory = gameServerManager.getServerConfigurationFactory();
		GameServer gameServer = gameApi.getGameServer();
		GameAliveFactory gameAliveFactory = new GameAliveFactory(gameApi.getServer().getServerName(), isJoinable(), Bukkit.getOnlinePlayers().size(), gameServer.getMaxPlayers());
		gameApi.getRabbitSpeaker().sendSyncUTF8Publisher("networkdocker.instance.keepalive", gameServerManager.getGson().toJson(gameAliveFactory), 5000, false);
	}
	
	public void sendStopPacket() {
		GameAPI gameApi = GameAPI.getAPI();
		//ServerConfigurationFactory serverConfigurationFactory = gameServerManager.getServerConfigurationFactory();
		gameApi.getRabbitSpeaker().sendSyncUTF8Publisher("networkdocker.instance.stop", gameApi.getServer().getServerName(), 5000, false);
	}
	
	private boolean isJoinable() {
		GameState gameState = GamePlugin.getInstance().getGameServer().getGameState();
		return gameState.equals(GameState.WAITING);
	}
	
	public void setFirstServer() {
		String[] split = GameAPI.getAPI().getServer().getServerName().split("_");
		if (split.length < 2) return;
		String af = split[1];
		long serverId = Integer.parseInt(af);
		OptionalInt optionalInt = Arrays.stream(new File("..").listFiles()).mapToInt((file) -> {
			if(file.isDirectory())
				try {
					return Integer.parseInt(file.getName());
				} catch(Exception unused){}
			return -1;
		}).filter((value) ->  value > 0).min();
		
		this.setFirstServer(optionalInt.isPresent() && optionalInt.getAsInt() == serverId);
	}

}

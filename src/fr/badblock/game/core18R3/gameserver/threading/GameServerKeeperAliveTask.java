package fr.badblock.game.core18R3.gameserver.threading;

import java.io.File;
import java.util.Arrays;
import java.util.OptionalInt;

import org.bukkit.Bukkit;

import fr.badblock.docker.factories.GameAliveFactory;
import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.gameserver.DevAliveFactory;
import fr.badblock.game.core18R3.gameserver.GameServerManager;
import fr.badblock.game.core18R3.jsonconfiguration.data.GameServerConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.GameServer;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameServerKeeperAliveTask extends GameServerTask {
	private static boolean openStaff = false;
	
	public static boolean switchOpenStaff()
	{
		return (openStaff = !openStaff);
	}
	
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
		if (BukkitUtils.getPlayers().size() >= Bukkit.getMaxPlayers()) return false;
		GameState gameState = GamePlugin.getInstance().getGameServer().getGameState();
		if (!Bukkit.getServerName().startsWith("speeduhc") && !Bukkit.getServerName().startsWith("sg")) {
			if (gameState.equals(GameState.RUNNING) && GameAPI.getAPI().getGameServer().isJoinableWhenRunning()) {
				if (!GameAPI.getAPI().getTeams().isEmpty()) {
					// team pas full mais team avec > 0
					long count = GameAPI.getAPI().getTeams().stream().filter(team -> team.playersCurrentlyOnline() < team.getMaxPlayers() && team.playersCurrentlyOnline() > 0 && !team.isDead()).count();
					if (count == 0) return false;
					return GameAPI.isJoinable();
				}
				return GameAPI.isJoinable();
			}
		}
		return gameState.equals(GameState.WAITING);
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
		sendDevSignal(true, addedPlayers);
		gameApi.getRabbitSpeaker().sendAsyncUTF8Publisher("networkdocker.instance.keepalive", gameServerManager.getGson().toJson(gameAliveFactory), 5000, false);
	}

	public void sendStopPacket() {
		GameAPI gameApi = GameAPI.getAPI();
		// ServerConfigurationFactory serverConfigurationFactory =
		// gameServerManager.getServerConfigurationFactory();
		sendDevSignal(false, 0);
		gameApi.getRabbitSpeaker().sendSyncUTF8Publisher("networkdocker.instance.stop", gameApi.getServer().getServerName(), 5000, false);
		gameApi.getRabbitSpeaker().cut();
	}

	public void setFirstServer() {
		String[] split = GameAPI.getAPI().getServer().getServerName().split("_");
		if (split.length < 2)
			return;
		String af = split[1];
		try
		{
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
		catch(Exception e){}
	}
	
	private void sendDevSignal(boolean open, int addedPlayers)
	{
		GameAPI gameApi = GameAPI.getAPI();
		GameServerManager gameServerManager = this.getGameServerManager();
		GameServer gameServer = gameApi.getGameServer();

		if(gameApi.getRunType() != RunType.DEV)
			return;
		
		DevAliveFactory devAliveFactory = new DevAliveFactory(gameApi.getServer().getServerName(), open, Bukkit.getOnlinePlayers().size() + addedPlayers, gameServer.getMaxPlayers(), openStaff);
		System.out.println(devAliveFactory);
		gameApi.getRabbitSpeaker().sendAsyncUTF8Publisher("dev", gameServerManager.getGson().toJson(devAliveFactory), 5000, false);
	}

}

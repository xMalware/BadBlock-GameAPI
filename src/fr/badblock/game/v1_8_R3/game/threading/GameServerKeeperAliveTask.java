package fr.badblock.game.v1_8_R3.game.threading;

import java.io.File;
import java.util.Arrays;
import java.util.OptionalInt;

import org.bukkit.Bukkit;

import fr.badblock.game.v1_8_R3.jsonconfiguration.APIConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.threading.TaskManager;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class GameServerKeeperAliveTask extends GameServerTask {
	
	private boolean	firstServer;
	
	public GameServerKeeperAliveTask(APIConfig apiConfig) {
		TaskManager.scheduleSyncRepeatingTask("gameServerKeeperAlive", this, 0, apiConfig.ticksBetweenKeepAlives);
	}

	@Override
	public void run() {
		this.setFirstServer();
		if (this.getGameServerManager().getJoinTime() < System.currentTimeMillis() && Bukkit.getOnlinePlayers().size() == 0 && !this.isFirstServer()) {
			GameAPI.logColor("&b[GameServer] &cNobody during a part of 30 minutes, shutdown...");
			Bukkit.shutdown();
			return;
		}
		this.getGameServerManager().keepAlive();
	}

	public void setFirstServer() {
		String string = this.getGameServerManager().getLogsFile().split("/")[0];
		int serverId = Integer.parseInt(Bukkit.getServerName().replace(string, ""));

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

package fr.badblock.game.v1_8_R3.game.threading;

import org.bukkit.Bukkit;

import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.game.v1_8_R3.game.GameServerManager;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class GameServerKeeperAliveTask implements Runnable {
	
	public GameServerKeeperAliveTask() {
		TaskManager.scheduleSyncRepeatingTask("gameServerKeeperAlive", this, 0, 200);
	}

	@Override
	public void run() {
		GamePlugin gamePlugin = GamePlugin.getInstance();
		GameServerManager gameServerManager = gamePlugin.getGameServerManager();
		if (gameServerManager.getJoinTime() < System.currentTimeMillis() && Bukkit.getOnlinePlayers().size() == 0 && !gameServerManager.isTheFirstServer()) {
			GameAPI.logColor("&b[GameServer] &cNobody during a part of 30 minutes, shutdown...");
			Bukkit.shutdown();
			return;
		}
		gameServerManager.keepAlive();
	}

}

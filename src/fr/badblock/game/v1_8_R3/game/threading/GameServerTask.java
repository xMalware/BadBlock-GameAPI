package fr.badblock.game.v1_8_R3.game.threading;

import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.game.v1_8_R3.game.GameServerManager;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public abstract class GameServerTask implements Runnable {
	
	private GamePlugin		  gamePlugin 		= GamePlugin.getInstance();
	private GameServerManager gameServerManager = gamePlugin.getGameServerManager();

}

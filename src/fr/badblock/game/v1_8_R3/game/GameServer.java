package fr.badblock.game.v1_8_R3.game;

import org.bukkit.Bukkit;

import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.gameapi.game.GameState;
import lombok.Getter;
import lombok.Setter;

/**
 * GameServer sous l'override de l'API
 * @author xMalware
 * @author LeLanN
 */
@Getter public class GameServer implements fr.badblock.gameapi.game.GameServer {
	private GameState gameState  = GameState.WAITING;
	@Setter
	private int 	  maxPlayers = Bukkit.getMaxPlayers();
	
	@Override
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		GamePlugin.getInstance().getGameServerManager().keepAlive();
	}
}

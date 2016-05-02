package fr.badblock.game.v1_8_R3.game;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import com.google.common.collect.Maps;

import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * GameServer sous l'override de l'API
 * @author xMalware
 * @author LeLanN
 */
@Getter@Setter public class GameServer extends BadListener implements fr.badblock.gameapi.game.GameServer {
	private GameState 						 gameState  	= GameState.WAITING;
	private int 	  				    	 maxPlayers 	= Bukkit.getMaxPlayers();
	private WhileRunningConnectionTypes 	 type 	    	= WhileRunningConnectionTypes.SPECTATOR;
	
	private Map<UUID, BadblockOfflinePlayer> players		= Maps.newConcurrentMap();
	
	@Override
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		
		if(gameState == GameState.FINISHED){
			cancelReconnectionInvatations();
		}
		
		GamePlugin.getInstance().getGameServerManager().keepAlive();
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();
		
		
	}
	
	@Override
	public void whileRunningConnection(WhileRunningConnectionTypes type) {
		if(type == WhileRunningConnectionTypes.SPECTATOR){
			cancelReconnectionInvatations(); // on annule tout de même au passage
		} else if(type == WhileRunningConnectionTypes.BACKUP){
			this.type = type;
		}
	}

	@Override
	public void cancelReconnectionInvatations() {
		type = WhileRunningConnectionTypes.SPECTATOR;
		
		for(UUID uniqueId : players.keySet()){
			GameAPI.getAPI().getLadderDatabase().sendReconnectionInvitation(uniqueId, false);
		}
		
		players.clear();
	}
}

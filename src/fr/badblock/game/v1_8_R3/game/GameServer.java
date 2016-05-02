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
import fr.badblock.gameapi.databases.LadderSpeaker;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import lombok.Setter;

/**
 * Overrided GameServer interface; for r1.8_3builds
 * @authors xMalware & LeLanN
 */
@Getter@Setter public class GameServer extends BadListener implements fr.badblock.gameapi.game.GameServer {
	
	private GameState 						 gameState  	= GameState.WAITING;
	private int 	  				    	 maxPlayers 	= Bukkit.getMaxPlayers();
	private WhileRunningConnectionTypes 	 type 	    	= WhileRunningConnectionTypes.SPECTATOR;
	
	private Map<UUID, BadblockOfflinePlayer> players		= Maps.newConcurrentMap();
	
	@Override
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
		if (gameState == GameState.FINISHED) cancelReconnectionInvitations();
		
		GamePlugin.getInstance().getGameServerManager().keepAlive();
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		BadblockPlayer player = (BadblockPlayer) event.getPlayer();
		
		
	}
	
	@Override
	public void whileRunningConnection(WhileRunningConnectionTypes type) {
		if (type.equals(WhileRunningConnectionTypes.SPECTATOR)) {
			cancelReconnectionInvitations(); // on annule tout de même au passage
			return;
		}else if (type.equals(WhileRunningConnectionTypes.BACKUP))
			this.type = type;
	}

	@Override
	public void cancelReconnectionInvitations() {
		type = WhileRunningConnectionTypes.SPECTATOR;
		
		GameAPI gameApi = GameAPI.getAPI();
		LadderSpeaker ladderSpeaker = gameApi.getLadderDatabase();
		players.keySet().forEach(uuid -> ladderSpeaker.sendReconnectionInvitation(uuid, false));
		
		players.clear();
	}
}

package fr.badblock.game.v1_8_R3.game;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.common.collect.Maps;

import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.game.v1_8_R3.players.GameBadblockPlayer;
import fr.badblock.game.v1_8_R3.players.GameOfflinePlayer;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.LadderSpeaker;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import lombok.Getter;
import lombok.Setter;

/**
 * Overrided GameServer interface
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
		
		if(!GameAPI.TEST_MODE)
			GamePlugin.getInstance().getGameServerManager().getGameServerKeeperAliveTask().keepAlive(gameState);
	}

	public void remember(BadblockPlayer player){
		GameOfflinePlayer offline = new GameOfflinePlayer((GameBadblockPlayer) player);
		players.put(player.getUniqueId(), offline);
	}
	
	@Override
	public void whileRunningConnection(WhileRunningConnectionTypes type) {
		this.type = type;
		
		if(type == WhileRunningConnectionTypes.SPECTATOR)
			cancelReconnectionInvitations();
	}

	@Override
	public void cancelReconnectionInvitations() {
		type = WhileRunningConnectionTypes.SPECTATOR;
		
		LadderSpeaker ladderSpeaker = GameAPI.getAPI().getLadderDatabase();
		
		players.keySet().forEach(uuid -> ladderSpeaker.sendReconnectionInvitation(uuid, false));
		players.clear();
	}

	@Override
	public void cancelReconnectionInvitations(BadblockTeam team) {
		LadderSpeaker ladderSpeaker = GameAPI.getAPI().getLadderDatabase();
		
		players.entrySet().forEach(entry -> {
			if(team.equals(entry.getValue().getTeam())){
				ladderSpeaker.sendReconnectionInvitation(entry.getKey(), false);
				players.remove(entry.getKey());
			}
		});
	}
}

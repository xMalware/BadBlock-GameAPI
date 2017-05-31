package fr.badblock.game.core18R3.tasks;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class GameStatisticsTask implements Runnable {

	private int		  tick;
	private GameState lastState;
	private int		  startedGamePlayers;
	private boolean	  fullAliveTeams;
	
	public GameStatisticsTask() {
		TaskManager.scheduleSyncRepeatingTask("gameStatisticsTask", this, 1, 1);
	}
	
	@Override
	public void run() {
		tick++;
		GameState state = GameAPI.getAPI().getGameServer().getGameState();
		int gamePlayers = BukkitUtils.getPlayers().size();
		if (lastState != null) {
			if (lastState.equals(GameState.RUNNING) && state.equals(GameState.FINISHED)) {
				if (gamePlayers < 2) {
					// Pas assez de joueurs
					increment(true, true);
				}else{
					if (!fullAliveTeams) {
						// Moins de joueurs qu'au démarrage de la partie
						increment(true, false);
					}else{
						// Pas de outch
						increment(false, false);
					}
				}
			}else if (lastState.equals(GameState.WAITING) && state.equals(GameState.RUNNING)) {
				startedGamePlayers = BukkitUtils.getPlayers().size();
			}else if (state.equals(GameState.RUNNING) && lastState.equals(GameState.RUNNING)) {
				if (tick == 20) {
					Collection<BadblockTeam> collection = GameAPI.getAPI().getTeams();
					if (collection.size() == 0) fullAliveTeams = gamePlayers >= startedGamePlayers; // Pas de système de team
					else {
						boolean tempFull = true;
						Iterator<BadblockTeam> iterator = collection.iterator();
						while (iterator.hasNext()) {
							BadblockTeam team = iterator.next();
							if (team != null && !team.isDead()) {
								if (team.getOnlinePlayers().size() < team.getMaxPlayers()) 
									tempFull = false;
							}
						}
						fullAliveTeams = tempFull;
					}
				}
			}
		}
		if (tick == 20) tick = 0;
		lastState = state;
	}
	
	public void increment(boolean outch, boolean notEnough) {
		if (!outch && notEnough) return;
		String gameName = GameAPI.getServerName().split("_")[0];
		String name = outch && notEnough ? "gameEndedNotEnoughPlayers" : outch ? "gameTeamsNotFull" : "";
		GameAPI.getAPI().getSqlDatabase().call("SELECT * FROM games WHERE gameName = '" + gameName + "'", SQLRequestType.QUERY, new Callback<ResultSet>() {

			@Override
			public void done(ResultSet result, Throwable error) {
				try {
					if (result.next()) {
						GameAPI.getAPI().getSqlDatabase().call("UPDATE games SET gameTotal=gameTotal+1" + (outch ? ", " + name + "=" + name + "+1" : "") + " WHERE gameName = '" + gameName + "'", SQLRequestType.UPDATE);
					}else{
						GameAPI.getAPI().getSqlDatabase().call("INSERT INTO games(gameName, gameTotal, gameEndedNotEnoughPlayers, gameTeamsNotFull) VALUES('" + gameName + "', '1', '" + (outch && notEnough ? "1" : "0") + "', '" + (outch && !notEnough ? "1" : "0") + "')", SQLRequestType.UPDATE);	
					}
				}catch(Exception err) {
					error.printStackTrace();
				}
			}
			
		});
	}
	
}

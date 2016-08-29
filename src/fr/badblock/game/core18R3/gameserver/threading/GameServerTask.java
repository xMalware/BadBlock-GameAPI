package fr.badblock.game.core18R3.gameserver.threading;

import java.util.TimerTask;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.gameserver.GameServerManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GameServerTask extends TimerTask implements Runnable {

	private GamePlugin gamePlugin = GamePlugin.getInstance();
	private GameServerManager gameServerManager = gamePlugin.getGameServerManager();

}

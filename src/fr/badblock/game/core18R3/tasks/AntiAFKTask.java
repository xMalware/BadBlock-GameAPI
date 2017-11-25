package fr.badblock.game.core18R3.tasks;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class AntiAFKTask implements Runnable {

	private GamePlugin	gamePlugin;

	public AntiAFKTask(GamePlugin gamePlugin) {
		this.gamePlugin = gamePlugin;
		TaskManager.scheduleSyncRepeatingTask("antiAfk", this, 20 * 30, 20 * 30);
	}

	@Override
	public void run() {
		if (!gamePlugin.isAntiAfk())
		{
			return;
		}

		for (BadblockPlayer player : BukkitUtils.getAllPlayers())
		{
			GameBadblockPlayer gamePlayer = (GameBadblockPlayer) player;
			if (gamePlayer.hasPermission("antiafk.bypass"))
			{
				continue;
			}
			
			if (gamePlayer.getMoveDist() <= 3 && GameAPI.getAPI().getGameServer().getGameState().equals(GameState.RUNNING))
			{
				gamePlayer.setVlAfk(gamePlayer.getVlAfk() + 1);
			}
			else
			{
				gamePlayer.setVlAfk(0);
			}
			gamePlayer.setMoveDist(0);

			if (gamePlayer.getVlAfk() >= 6)
			{
				gamePlayer.sendTranslatedMessage("antiafk.k");
				gamePlayer.sendPlayer("lobby");
				new TranslatableString("antiafk.kick", gamePlayer.getTabGroupPrefix().getAsLine(gamePlayer) + gamePlayer.getName()).broadcast();
			}
			else if (gamePlayer.getVlAfk() >= 2)
			{
				gamePlayer.sendTranslatedMessage("antiafk.warn", gamePlayer.getVlAfk() - 1);
			}

		}

	}

}

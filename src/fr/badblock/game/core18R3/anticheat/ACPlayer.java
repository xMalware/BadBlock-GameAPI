package fr.badblock.game.core18R3.anticheat;

import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.utils.selections.Vector3f;

public class ACPlayer implements InGameData {
	public Vector3f lastMove;
	public int		lastCount = 0;
	public boolean  wasWater  = false;
	
	public void kick(BadblockPlayer player, String msg){
		new BukkitRunnable(){
			@Override
			public void run() {
				player.kickPlayer(msg);
			}
		}.runTaskLater(GameAPI.getAPI(), 1L);
	} 
}

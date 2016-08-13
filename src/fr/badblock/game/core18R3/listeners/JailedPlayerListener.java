package fr.badblock.game.core18R3.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;

/**
 * Permet de bloquer certaines interractions au joueur qui le permettrai de sortir de son état de 'jail'
 * @author LeLanN
 */
public class JailedPlayerListener extends BadListener {
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e){
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();
		
		if(player.isJailed()){
			e.setCancelled(true);
		}
	}
}
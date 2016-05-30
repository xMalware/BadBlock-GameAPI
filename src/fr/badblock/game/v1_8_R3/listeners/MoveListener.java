package fr.badblock.game.v1_8_R3.listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.badblock.game.v1_8_R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.BadListener;

public class MoveListener extends BadListener {
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		
		GameBadblockPlayer player = (GameBadblockPlayer) e.getPlayer();
		
		if(player.getCenterJail() != null){
			
			Location loc = e.getTo().clone();
			loc.setY(player.getCenterJail().getY());
			
			if(loc.distance(player.getCenterJail()) >= player.getRadius()){
				e.setCancelled(true);
				
				player.teleport(player.getCenterJail());
				player.sendTranslatedMessage("game.move-toofar");
			}
			
		}
	}
}

package fr.badblock.game.v1_8_R3.listeners.packets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import fr.badblock.gameapi.events.PlayerSpectateEvent;
import fr.badblock.gameapi.packets.OutPacketListener;
import fr.badblock.gameapi.packets.out.play.PlayCamera;
import fr.badblock.gameapi.players.BadblockPlayer;

public class CameraListener extends OutPacketListener<PlayCamera> {

	@Override
	public void listen(BadblockPlayer player, PlayCamera packet) {
		Entity entity = null;
		
		for(Entity e : player.getWorld().getEntities()) {
			if(e.getEntityId() == packet.getEntityId()) {
				entity = e;
				break;
			}
		}
		
		if(entity == null){
			// Il s'agit d'une fake entité, donc fait par un plugin, on laisse
		} else {
			PlayerSpectateEvent e = new PlayerSpectateEvent(player, entity);
			
			Bukkit.getPluginManager().callEvent(e);
			
			if(e.isCancelled()){
				packet.setCancelled(true);
			}
		}
	}

}

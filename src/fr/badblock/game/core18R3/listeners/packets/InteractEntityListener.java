package fr.badblock.game.core18R3.listeners.packets;

import org.bukkit.Bukkit;

import fr.badblock.game.core18R3.fakeentities.FakeEntities;
import fr.badblock.gameapi.events.PlayerFakeEntityInteractEvent;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.InPacketListener;
import fr.badblock.gameapi.packets.in.play.PlayInUseEntity;
import fr.badblock.gameapi.players.BadblockPlayer;

public class InteractEntityListener extends InPacketListener<PlayInUseEntity> {

	@Override
	public void listen(BadblockPlayer player, PlayInUseEntity packet) {
		FakeEntity<?> entity = FakeEntities.retrieveFakeEntity(packet.getEntityId());
		
		if(entity != null){
			packet.setCancelled(true);
			
			Bukkit.getPluginManager().callEvent(new PlayerFakeEntityInteractEvent(player, entity, packet.getAction()));
		}
	}

}
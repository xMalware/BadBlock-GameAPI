package fr.badblock.game.core18R3.listeners.packets;

import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import fr.badblock.game.core18R3.fakeentities.FakeEntities;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
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
			for(BadblockPlayer pl : GameAPI.getAPI().getOnlinePlayers().stream().filter(p -> p.isDisguised() && p != player).collect(Collectors.toList())){
				GameBadblockPlayer gpl = (GameBadblockPlayer) pl;
				
				if(gpl.getDisguiseEntity().getId() == entity.getId()){
					packet.setEntityId(pl.getEntityId());
					return;
				}
			}
		}
		
		if (entity != null && player.getLastFakeEntityUsedTime() < System.currentTimeMillis()) {
			packet.setCancelled(true);
			player.useFakeEntity();
			Bukkit.getPluginManager().callEvent(new PlayerFakeEntityInteractEvent(player, entity, packet.getAction(), false));
		}
	}

}
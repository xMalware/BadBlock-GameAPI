package fr.badblock.game.core18R3.listeners.packets;

import org.bukkit.Bukkit;

import fr.badblock.game.core18R3.fakeentities.FakeEntities;
import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.PlayerFakeEntityInteractEvent;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.InPacketListener;
import fr.badblock.gameapi.packets.in.play.PlayInUseEntity;
import fr.badblock.gameapi.players.BadblockPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class InteractEntityListener extends InPacketListener<PlayInUseEntity> {

	@Override
	public void listen(BadblockPlayer player, PlayInUseEntity packet) {
		FakeEntity<?> entity = FakeEntities.retrieveFakeEntity(packet.getEntityId());
		
		if(entity != null){
			for(BadblockPlayer pl : GameAPI.getAPI().getOnlinePlayers()){
				if(!pl.isDisguised() || pl.getUniqueId().equals(player.getUniqueId()))
					continue;
			
				GameBadblockPlayer gpl = (GameBadblockPlayer) pl;
				
				if(gpl.getDisguiseEntity().getId() == entity.getId()){
					try {
						MinecraftServer.getServer().primaryThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					packet.setEntityId(pl.getEntityId());
					
					PacketPlayInUseEntity p = (PacketPlayInUseEntity) (((GameBadblockInPacket) packet).toNms());
					gpl.getHandle().playerConnection.a(p);
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
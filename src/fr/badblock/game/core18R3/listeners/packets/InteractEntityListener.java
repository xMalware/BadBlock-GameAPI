package fr.badblock.game.core18R3.listeners.packets;

import org.bukkit.Bukkit;

import fr.badblock.game.core18R3.fakeentities.FakeEntities;
import fr.badblock.gameapi.events.PlayerFakeEntityInteractEvent;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.InPacketListener;
import fr.badblock.gameapi.packets.in.play.PlayInUseEntity;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class InteractEntityListener extends InPacketListener<PlayInUseEntity> {

	@Override
	public void listen(BadblockPlayer player, PlayInUseEntity packet) {
		FakeEntity<?> entity = FakeEntities.retrieveFakeEntity(packet.getEntityId());

		/*if(entity != null){
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

					PacketPlayInUseEntity p = (PacketPlayInUseEntity) (((GameBadblockInPacket) packet).toNms());
					Reflector reflec = new Reflector(p);

					try {
						reflec.setFieldValue("a", pl.getEntityId());
					} catch (Exception e) {
						e.printStackTrace();
					}

					gpl.getHandle().playerConnection.a(p);
					return;
				}
			}
		}*/

		if (entity != null && packet != null && packet.getAction() != null && player != null && player.getLastFakeEntityUsedTime() < System.currentTimeMillis()) {
			TaskManager.runTask(new Runnable() {
				@Override
				public void run() {
					player.useFakeEntity();
					Bukkit.getPluginManager().callEvent(new PlayerFakeEntityInteractEvent(player, entity, packet.getAction(), false));
				}
			});
		}
	}

}
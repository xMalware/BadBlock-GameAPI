package fr.badblock.game.core18R3.packets.out;

import java.util.List;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.game.core18R3.watchers.GameWatcherEntity;
import fr.badblock.gameapi.packets.out.play.PlayEntityMetadata;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityMetadata extends GameBadblockOutPacket implements PlayEntityMetadata {
	private int 		  entityId = 0;
	private WatcherEntity watcher  = null;
	private List<DataWatcher.WatchableObject> tempWatcher;
	
	@SuppressWarnings("unchecked")
	public GamePlayEntityMetadata(PacketPlayOutEntityMetadata packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId    = (int) reflector.getFieldValue("a");
			tempWatcher = (List<DataWatcher.WatchableObject>) reflector.getFieldValue("b");
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", entityId);
		
		if(watcher != null)
			reflector.setFieldValue("b", ((GameWatcherEntity) watcher).convertToWatchables());
		else reflector.setFieldValue("b", tempWatcher);
		return packet;
	}
	
	@Override
	public Packet<?> buildPacket(BadblockPlayer player) throws Exception {
		PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata();
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", entityId);
		
		if(watcher != null)
			reflector.setFieldValue("b", ((GameWatcherEntity) watcher).convertToWatchables(player));
		else reflector.setFieldValue("b", tempWatcher);
		return packet;
	}
}

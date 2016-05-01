package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayAttachEntity;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayAttachEntity extends GameBadblockOutPacket implements PlayAttachEntity {
	private int entityId    = 0;
	private int vehicleId   = -1;
	private boolean leashes = false;
	
	public GamePlayAttachEntity(PacketPlayOutAttachEntity packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			leashes   = (int) reflector.getFieldValue("a") == 1;
			entityId  = (int) reflector.getFieldValue("b");
			vehicleId = (int) reflector.getFieldValue("c");
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity();
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", leashes ? 1 : 0);
		reflector.setFieldValue("b", entityId);
		reflector.setFieldValue("c", vehicleId);
		
		return packet;
	}
}

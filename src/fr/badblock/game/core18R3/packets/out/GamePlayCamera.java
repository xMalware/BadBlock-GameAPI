package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayCamera;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayCamera extends GameBadblockOutPacket implements PlayCamera {
	private int entityId;
	
	public GamePlayCamera(PacketPlayOutCamera packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId  = (int) reflector.getFieldValue("a");
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutCamera packet    = new PacketPlayOutCamera();
		Reflector			reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", entityId);
		
		return packet;
	}

}

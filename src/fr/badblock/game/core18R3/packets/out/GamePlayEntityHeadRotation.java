package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityHeadRotation;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityHeadRotation extends GameBadblockOutPacket implements PlayEntityHeadRotation {
	private int   entityId = 0;
	private float rotation = 0;
	
	public GamePlayEntityHeadRotation(PacketPlayOutEntityHeadRotation packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId   = (int) reflector.getFieldValue("a");
			rotation   = ((float) reflector.getFieldValue("a")) * 360F / 256;
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation();
		
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", (byte) MathHelper.d(rotation * 256F / 360F));
		
		return null;
	}
}

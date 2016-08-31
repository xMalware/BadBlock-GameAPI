package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityLook;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityLook extends GameBadblockOutPacket implements PlayEntityLook {
	private int   	entityId = 0;
	private float 	yaw	     = 0f;
	private float 	pitch	 = 0f;
	private boolean ground   = false;
	
	public GamePlayEntityLook(PacketPlayOutEntityLook packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId = (int) reflector.getFieldValue("a");
			yaw	     = (float) reflector.getFieldValue("e") / 256 * 360;
			pitch    = (float) reflector.getFieldValue("f") / 256 * 360;
			ground	 = (boolean) reflector.getFieldValue("g");
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutEntityLook(entityId, (byte) (yaw * 256 / 360), (byte) (pitch * 256 / 360), ground);
	}
}

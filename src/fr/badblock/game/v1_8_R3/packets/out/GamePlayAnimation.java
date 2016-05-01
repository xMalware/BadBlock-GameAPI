package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayAnimation;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayAnimation extends GameBadblockOutPacket implements PlayAnimation {
	private int		  entityId  = 0;
	private Animation animation = Animation.SWING_ARM;
	

	public GamePlayAnimation(PacketPlayOutAnimation packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId  = (int) reflector.getFieldValue("a");
			animation = Animation.getFromId((int) reflector.getFieldValue("b"));
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
		
		Reflector reflector = new Reflector(packet);
		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", animation.getId());

		return packet;
	}
}

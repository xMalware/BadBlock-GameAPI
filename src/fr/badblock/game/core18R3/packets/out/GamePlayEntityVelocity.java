package fr.badblock.game.core18R3.packets.out;

import org.bukkit.util.Vector;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityVelocity;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityVelocity;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityVelocity extends GameBadblockOutPacket implements PlayEntityVelocity {
	private int    entityId = 0;
	private Vector velocity = null;
	
	public GamePlayEntityVelocity(PacketPlayOutEntityVelocity packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId   = (int) reflector.getFieldValue("a");
			velocity   = new Vector(((double) reflector.getFieldValue("b")) / 8000.D,
				((double) reflector.getFieldValue("c")) / 8000.D,
				((double) reflector.getFieldValue("d")) / 8000.D
			);
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutEntityVelocity(entityId, velocity.getX(), velocity.getY(), velocity.getZ());
	}

}

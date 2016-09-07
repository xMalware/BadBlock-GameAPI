package fr.badblock.game.core18R3.packets.out;

import org.bukkit.util.Vector;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityRelativeMove;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutRelEntityMove;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityRelativeMove extends GameBadblockOutPacket implements PlayEntityRelativeMove {
	private int     entityId = 0;
	private Vector  move	 = null;
	private boolean onGround = true;
	
	public GamePlayEntityRelativeMove(PacketPlayOutRelEntityMove packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId   = (int) reflector.getFieldValue("a");
			move	   = new Vector(
					(double) reflector.getFieldValue("b") / 32.0D,
					(double) reflector.getFieldValue("c") / 32.0D,
					(double) reflector.getFieldValue("d") / 32.0D
				);
			onGround   = (boolean) reflector.getFieldValue("g");
		} catch(Exception e){}
	}	
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutRelEntityMove(entityId, 
				(byte) (move.getX() * 32),
				(byte) (move.getY() * 32),
				(byte) (move.getZ() * 32),
				onGround);
	}

}

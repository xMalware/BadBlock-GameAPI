package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayAbilities;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutAbilities;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayAbilities extends GameBadblockOutPacket implements PlayAbilities {
	private boolean godmoded;
	private boolean flying;
	private boolean allowedToFly;
	private boolean creative;

	private float flyingSpeed;
	private float walkingSpeed;
	
	public GamePlayAbilities(PacketPlayOutAbilities packet){
		godmoded 	 = packet.a();
		flying   	 = packet.b();
		allowedToFly = packet.c();
		creative     = packet.d();
		
		Reflector reflector = new Reflector(packet);
		try {
			flyingSpeed  = (float) reflector.getFieldValue("e");
			walkingSpeed = (float) reflector.getFieldValue("f");
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutAbilities packet = new PacketPlayOutAbilities();
		packet.a(godmoded);
		packet.b(flying);
		packet.c(allowedToFly);
		packet.d(creative);
		
		packet.a(flyingSpeed);
		packet.b(walkingSpeed);
		
		return packet;
	}

}

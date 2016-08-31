package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityExperienceOrb;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityExperienceOrb;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlaySpawnEntityExperienceOrb extends GameBadblockOutPacket implements PlaySpawnEntityExperienceOrb {
	private int 	 entityId;
	private Vector3f position;
	private short    orbCount;

	public GamePlaySpawnEntityExperienceOrb(PacketPlayOutSpawnEntityExperienceOrb packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId     = (int) reflector.getFieldValue("a");
			position	 = new Vector3f((double) reflector.getFieldValue("b") / 32.0D,
							(double) reflector.getFieldValue("c") / 32.0D,
							(double) reflector.getFieldValue("d") / 32.0D);
			orbCount	 = (short) reflector.getFieldValue("e");
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutSpawnEntityExperienceOrb packet = new PacketPlayOutSpawnEntityExperienceOrb();
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", (int) (position.getX() * 32.0D));
		reflector.setFieldValue("c", (int) (position.getY() * 32.0D));
		reflector.setFieldValue("d", (int) (position.getZ() * 32.0D));
		reflector.setFieldValue("e", (int) orbCount);
		
		return null;
	}
	
}

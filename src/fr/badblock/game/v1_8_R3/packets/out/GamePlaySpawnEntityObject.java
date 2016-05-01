package fr.badblock.game.v1_8_R3.packets.out;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityObject;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntity;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlaySpawnEntityObject extends GameBadblockOutPacket implements PlaySpawnEntityObject {
	private int 			 entityId = 0;
	private SpawnableObjects type	  = SpawnableObjects.ACTIVATED_TNT;
	private int 			 data	  = 0;
	private Location 		 location = null;
	private Vector 			 velocity = null;
	
	public GamePlaySpawnEntityObject(PacketPlayOutSpawnEntity packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId     = (int) reflector.getFieldValue("a");
			type		 = SpawnableObjects.getByValue((byte) reflector.getFieldValue("j"));
			location	 = new Location(Bukkit.getWorlds().get(0),
							(double) reflector.getFieldValue("b") / 32.0D,
							(double) reflector.getFieldValue("c") / 32.0D,
							(double) reflector.getFieldValue("d") / 32.0D,
							(float)  reflector.getFieldValue("h") / 256.0F * 360.0F,
							(float)  reflector.getFieldValue("i") / 256.0F * 360.0F);
			velocity	 = new Vector(
								(double) reflector.getFieldValue("e") / 8000.0D,
								(double) reflector.getFieldValue("f") / 8000.0D,
								(double) reflector.getFieldValue("g") / 8000.0D
						   );
			data	     = (int) reflector.getFieldValue("k");

		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity();
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", MathHelper.floor(location.getX() * 32.0D));
		reflector.setFieldValue("c", MathHelper.floor(location.getY() * 32.0D));
		reflector.setFieldValue("d", MathHelper.floor(location.getZ() * 32.0D));
		
		if(velocity != null){
			reflector.setFieldValue("e", (int)(velocity.getX() * 8000.0D));
			reflector.setFieldValue("f", (int)(velocity.getX() * 8000.0D));
			reflector.setFieldValue("g", (int)(velocity.getX() * 8000.0D));
		}
		
		reflector.setFieldValue("h", ((byte)(int)(location.getYaw() * 256.0F / 360.0F)));
		reflector.setFieldValue("i", ((byte)(int)(location.getPitch() * 256.0F / 360.0F)));
		reflector.setFieldValue("j", type.getId());
		reflector.setFieldValue("k", data);
		
		return packet;
	}

}

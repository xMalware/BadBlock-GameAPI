package fr.badblock.game.core18R3.packets.out;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.game.core18R3.watchers.GameWatcherEntity;
import fr.badblock.gameapi.packets.out.play.PlayNamedEntitySpawn;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayNamedEntitySpawn extends GameBadblockOutPacket implements PlayNamedEntitySpawn {
	private int 			   entityId 	= 0;
	private UUID			   uniqueId		= UUID.randomUUID();
	private Location		   location 	= null;
	private Material		   itemInHand   = Material.AIR;
	private WatcherEntity	   watchers		= null;
	private DataWatcher		   tempWatcher  = null;
	
	@SuppressWarnings("deprecation")
	public GamePlayNamedEntitySpawn(PacketPlayOutNamedEntitySpawn packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId     = (int) reflector.getFieldValue("a");
			uniqueId     = (UUID) reflector.getFieldValue("b");
			location	 = new Location(Bukkit.getWorlds().get(0),
							(double) reflector.getFieldValue("c") / 32.0D,
							(double) reflector.getFieldValue("d") / 32.0D,
							(double) reflector.getFieldValue("e") / 32.0D,
							(float)  reflector.getFieldValue("f") / 256.0F * 360.0F,
							(float)  reflector.getFieldValue("g") / 256.0F * 360.0F);
			itemInHand	 = Material.getMaterial((int) reflector.getFieldValue("h"));
			tempWatcher	 = (DataWatcher) reflector.getFieldValue("i");
		} catch(Exception e){}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", uniqueId);
		reflector.setFieldValue("c", MathHelper.floor(location.getX() * 32.0D));
		reflector.setFieldValue("d", MathHelper.floor(location.getY() * 32.0D));
		reflector.setFieldValue("e", MathHelper.floor(location.getZ() * 32.0D));
		
		reflector.setFieldValue("f", ((byte)(int)(location.getYaw() * 256.0F / 360.0F)));
		reflector.setFieldValue("g", ((byte)(int)(location.getPitch() * 256.0F / 360.0F)));
		reflector.setFieldValue("h", itemInHand.getId());
		
		if(watchers != null)
			reflector.setFieldValue("i", ((GameWatcherEntity) watchers).convertToDatawatcher());
		else reflector.setFieldValue("i", tempWatcher);
		
		return packet;
	}	
}

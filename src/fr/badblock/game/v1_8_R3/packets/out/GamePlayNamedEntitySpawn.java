package fr.badblock.game.v1_8_R3.packets.out;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayNamedEntitySpawn;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayNamedEntitySpawn extends GameBadblockOutPacket implements PlayNamedEntitySpawn {
	private Player player = null;

	public GamePlayNamedEntitySpawn(PacketPlayOutNamedEntitySpawn packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			UUID uniqueId   = (UUID) reflector.getFieldValue("b");
			player			= Bukkit.getPlayer(uniqueId);
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle());
	}
}

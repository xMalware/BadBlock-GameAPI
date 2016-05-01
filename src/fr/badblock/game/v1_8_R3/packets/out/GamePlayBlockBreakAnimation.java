package fr.badblock.game.v1_8_R3.packets.out;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayBlockBreakAnimation;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayBlockBreakAnimation extends GameBadblockOutPacket implements PlayBlockBreakAnimation {
	private int	  entityId = -1;
	private Block block    = null;
	private int	  state    = 0;

	public GamePlayBlockBreakAnimation(PacketPlayOutBlockBreakAnimation packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId  = (int) reflector.getFieldValue("a");
			BlockPosition pos = (BlockPosition) reflector.getFieldValue("b");
			block     = Bukkit.getWorlds().get(0).getBlockAt(pos.getX(), pos.getY(), pos.getZ());
			state 	  = (int) reflector.getFieldValue("c");
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		BlockPosition position = new BlockPosition(block.getX(), block.getY(), block.getZ());
		return new PacketPlayOutBlockBreakAnimation(entityId, position, state);
	}
}

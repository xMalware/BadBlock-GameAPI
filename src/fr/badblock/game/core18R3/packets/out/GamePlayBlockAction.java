package fr.badblock.game.core18R3.packets.out;

import org.bukkit.Material;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayBlockAction;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockAction;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayBlockAction extends GameBadblockOutPacket implements PlayBlockAction {
	private Vector3f blockPosition;
	private byte	 byte1;
	private byte	 byte2;
	private Material blockType;

	@SuppressWarnings("deprecation")
	public GamePlayBlockAction(PacketPlayOutBlockAction packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			BlockPosition block = (BlockPosition) reflector.getFieldValue("a");
			blockPosition = new Vector3f(block.getX(), block.getY(), block.getZ());
			
			blockType	  = Material.getMaterial(Block.getId((Block) reflector.getFieldValue("d")));
			byte1		  = (byte) reflector.getFieldValue("b");
			byte2		  = (byte) reflector.getFieldValue("c");
		} catch(Exception e){}
	}
	
	
	@Override@SuppressWarnings("deprecation")
	public Packet<?> buildPacket() throws Exception {
		Block 		  block  = Block.getById(blockType.getId());
		BlockPosition pos    = new BlockPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
		
		Packet<?>	  packet = new PacketPlayOutBlockAction(pos, block, (int) byte1, (int) byte2);
		
		return packet;
	}

}

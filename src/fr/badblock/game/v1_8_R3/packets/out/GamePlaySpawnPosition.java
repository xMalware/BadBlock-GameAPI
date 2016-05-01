package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlaySpawnPosition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnPosition;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlaySpawnPosition extends GameBadblockOutPacket implements PlaySpawnPosition {
	
	private Packet<?> packet;
	
	public GamePlaySpawnPosition(PacketPlayOutSpawnPosition packet){
		this.packet = packet;
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return packet;
	}

}

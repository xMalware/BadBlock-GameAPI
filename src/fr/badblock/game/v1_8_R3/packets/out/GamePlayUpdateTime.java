package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayUpdateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutUpdateTime;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayUpdateTime extends GameBadblockOutPacket implements PlayUpdateTime {
	
	private Packet<?> packet;
	
	public GamePlayUpdateTime(PacketPlayOutUpdateTime packet){
		this.packet = packet;
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return packet;
	}

}

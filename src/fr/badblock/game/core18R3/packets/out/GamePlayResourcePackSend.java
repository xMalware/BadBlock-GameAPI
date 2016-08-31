package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayResourcePackSend;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutResourcePackSend;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayResourcePackSend extends GameBadblockOutPacket implements PlayResourcePackSend {
	
	private Packet<?> packet;
	
	public GamePlayResourcePackSend(PacketPlayOutResourcePackSend packet){
		this.packet = packet;
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return packet;
	}

}

package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayTransaction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayTransaction extends GameBadblockOutPacket implements PlayTransaction {
	
	private Packet<?> packet;
	
	public GamePlayTransaction(PacketPlayOutTransaction packet){
		this.packet = packet;
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return packet;
	}

}

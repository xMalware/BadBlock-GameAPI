package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityPainting;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityPainting;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlaySpawnEntityPainting extends GameBadblockOutPacket implements PlaySpawnEntityPainting {

	private Packet<?> packet;

	public GamePlaySpawnEntityPainting(PacketPlayOutSpawnEntityPainting packet) {
		this.packet = packet;
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		return packet;
	}

}

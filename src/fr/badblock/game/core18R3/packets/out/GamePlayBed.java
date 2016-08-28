package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayBed;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayBed extends GameBadblockOutPacket implements PlayBed {

	private Packet<?> packet;

	public GamePlayBed(PacketPlayOutBed packet) {
		this.packet = packet;
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		return packet;
	}

}

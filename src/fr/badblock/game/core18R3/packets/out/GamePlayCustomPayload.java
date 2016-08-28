package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayCustomPayload;
import fr.badblock.gameapi.utils.reflection.Reflector;
import io.netty.buffer.Unpooled;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayCustomPayload extends GameBadblockOutPacket implements PlayCustomPayload {
	private String channel;
	private byte[] data;

	public GamePlayCustomPayload(PacketPlayOutCustomPayload packet) {
		Reflector reflector = new Reflector(packet);

		try {
			channel = (String) reflector.getFieldValue("a");
			data = ((PacketDataSerializer) reflector.getFieldValue("b")).array();
		} catch (Exception e) {
		}

	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutCustomPayload(channel, new PacketDataSerializer(Unpooled.copiedBuffer(data)));
	}
}

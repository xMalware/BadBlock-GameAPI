package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityStatus;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityStatus extends GameBadblockOutPacket implements PlayEntityStatus {
	private int entityId = 0;
	private EntityStatus status = EntityStatus.EATING_ACCEPTED;

	public GamePlayEntityStatus(PacketPlayOutEntityStatus packet) {
		Reflector reflector = new Reflector(packet);

		try {
			entityId = (int) reflector.getFieldValue("a");
			status = EntityStatus.getByValue((byte) reflector.getFieldValue("b"));
		} catch (Exception e) {
		}
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();
		Reflector reflector = new Reflector(packet);

		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", status.getValue());

		return packet;
	}

}

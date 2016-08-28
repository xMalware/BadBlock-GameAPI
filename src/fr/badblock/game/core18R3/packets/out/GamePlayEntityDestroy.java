package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityDestroy;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityDestroy extends GameBadblockOutPacket implements PlayEntityDestroy {
	private int[] entities;

	public GamePlayEntityDestroy(PacketPlayOutEntityDestroy packet) {
		Reflector reflector = new Reflector(packet);

		try {
			entities = (int[]) reflector.getFieldValue("a");
		} catch (Exception e) {
		}
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutEntityDestroy(entities);
	}
}

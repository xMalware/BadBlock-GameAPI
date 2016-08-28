package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayNamedSoundEffect;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayNamedSoundEffect extends GameBadblockOutPacket implements PlayNamedSoundEffect {
	private String soundName;
	private Vector3f position;
	private float volume;
	private byte pitch;

	public GamePlayNamedSoundEffect(PacketPlayOutNamedSoundEffect packet) {
		Reflector reflector = new Reflector(packet);

		try {
			soundName = (String) reflector.getFieldValue("a");
			position = new Vector3f((double) reflector.getFieldValue("b") / 8.0D,
					(double) reflector.getFieldValue("c") / 8.0D, (double) reflector.getFieldValue("d") / 8.0D);
			volume = (float) reflector.getFieldValue("e");
			pitch = (byte) reflector.getFieldValue("f");
		} catch (Exception e) {
		}
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutNamedSoundEffect(soundName, position.getX(), position.getY(), position.getZ(), volume,
				(pitch) / 63.0F);
	}

}

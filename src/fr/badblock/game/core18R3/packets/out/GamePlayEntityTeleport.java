package fr.badblock.game.core18R3.packets.out;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityTeleport;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityTeleport extends GameBadblockOutPacket implements PlayEntityTeleport {
	private int entityId = 0;
	private Location to = null;
	private boolean onGround = false;

	public GamePlayEntityTeleport(PacketPlayOutEntityTeleport packet) {
		Reflector reflector = new Reflector(packet);

		try {
			entityId = (int) reflector.getFieldValue("a");
			to = new Location(Bukkit.getWorlds().get(0), ((double) reflector.getFieldValue("b")) / 32.0D,
					((double) reflector.getFieldValue("c")) / 32.0D, ((double) reflector.getFieldValue("d")) / 32.0D,
					((float) reflector.getFieldValue("e")) / 256.0F * 360.0F,
					((float) reflector.getFieldValue("f")) / 256.0F * 360.0F);
			onGround = (boolean) reflector.getFieldValue("g");
		} catch (Exception e) {
		}
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
		Reflector reflector = new Reflector(packet);

		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", MathHelper.floor(to.getX() * 32.0D));
		reflector.setFieldValue("c", MathHelper.floor(to.getY() * 32.0D));
		reflector.setFieldValue("d", MathHelper.floor(to.getZ() * 32.0D));
		reflector.setFieldValue("e", (byte) (to.getYaw() * 256.0F / 360.0F));
		reflector.setFieldValue("f", (byte) (to.getPitch() * 256.0F / 360.0F));
		reflector.setFieldValue("g", onGround);

		return packet;
	}
}
package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInResourcePackStatus;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_8_R3.PacketPlayInResourcePackStatus.EnumResourcePackStatus;

@Getter
public class GamePlayInResourcePackStatus extends GameBadblockInPacket implements PlayInResourcePackStatus {
	private String hash;
	private ResourcePackStatus status;

	public GamePlayInResourcePackStatus(PacketPlayInResourcePackStatus packet) {
		super(packet);
		Reflector reflector = new Reflector(packet);
		try {
			this.hash = (String) reflector.getFieldValue("a");
			this.status = ResourcePackStatus.valueOf(((EnumResourcePackStatus) reflector.getFieldValue("b")).name());
		} catch (Exception e) {
		}
	}
}

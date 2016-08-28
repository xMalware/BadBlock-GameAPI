package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInUpdateSign;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;

@Getter
public class GamePlayInUpdateSign extends GameBadblockInPacket implements PlayInUpdateSign {
	private Vector3f position;
	private BaseComponent[] line1, line2, line3, line4;

	public GamePlayInUpdateSign(PacketPlayInUpdateSign packet) {
		super(packet);
		this.position = new Vector3f(packet.a().getX(), packet.a().getY(), packet.a().getZ());
		this.line1 = fromChat(packet.b()[0]);
		this.line2 = fromChat(packet.b()[1]);
		this.line3 = fromChat(packet.b()[2]);
		this.line4 = fromChat(packet.b()[3]);
	}
}

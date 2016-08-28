package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInCustomPayload;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;

@Getter
public class GamePlayInCustomPayload extends GameBadblockInPacket implements PlayInCustomPayload {
	private String channel;
	private byte[] data;

	public GamePlayInCustomPayload(PacketPlayInCustomPayload packet) {
		super(packet);
		this.channel = packet.a();
		this.data = packet.b().array();
	}
}

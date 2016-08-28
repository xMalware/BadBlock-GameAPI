package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInChat;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;

@Getter
public class GamePlayInChat extends GameBadblockInPacket implements PlayInChat {
	private String message;

	public GamePlayInChat(PacketPlayInChat packet) {
		super(packet);
		this.message = packet.a();
	}
}

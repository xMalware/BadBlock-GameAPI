package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInKeepAlive;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;

@Getter
public class GamePlayInKeepAlive extends GameBadblockInPacket implements PlayInKeepAlive {
	private int keepAliveId;

	public GamePlayInKeepAlive(PacketPlayInKeepAlive packet) {
		super(packet);
		this.keepAliveId = packet.a();
	}
}

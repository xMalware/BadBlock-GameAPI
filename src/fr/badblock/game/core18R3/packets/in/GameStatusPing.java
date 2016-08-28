package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.StatusPing;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketStatusInPing;

@Getter
public class GameStatusPing extends GameBadblockInPacket implements StatusPing {
	private long longValue;

	public GameStatusPing(PacketStatusInPing packet) {
		super(packet);
		this.longValue = packet.a();
	}
}

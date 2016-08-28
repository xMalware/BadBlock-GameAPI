package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.LoginStart;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketLoginInStart;

@Getter
public class GameLoginStart extends GameBadblockInPacket implements LoginStart {
	private String userName;

	public GameLoginStart(PacketLoginInStart packet) {
		super(packet);
		this.userName = packet.a().getName();
	}

}

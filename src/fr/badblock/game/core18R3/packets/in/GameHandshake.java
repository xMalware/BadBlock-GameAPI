package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.Handshake;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketHandshakingInSetProtocol;

@Getter public class GameHandshake extends GameBadblockInPacket implements Handshake {
	private int 	  protocolVersion;
	private String    serverAddress;
	private int 	  serverPort;
	private NextState nextState;
	
	public GameHandshake(PacketHandshakingInSetProtocol packet){
		super(packet);
		protocolVersion = packet.b();
		serverAddress   = packet.hostname;
		serverPort		= packet.port;
		nextState		= NextState.getById(packet.a().a());
	}
}

package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInTabComplete;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;

@Getter public class GamePlayInTabComplete extends GameBadblockInPacket implements PlayInTabComplete {
	private String message;
	
	public GamePlayInTabComplete(PacketPlayInTabComplete packet){
		super(packet);
		message = packet.a();
	}
}

package fr.badblock.game.v1_8_R3.packets.in;

import fr.badblock.game.v1_8_R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.StatusRequest;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketStatusInStart;

@Getter public class GameStatusRequest extends GameBadblockInPacket implements StatusRequest {
	public GameStatusRequest(PacketStatusInStart packet){
		super(packet);
	}
}
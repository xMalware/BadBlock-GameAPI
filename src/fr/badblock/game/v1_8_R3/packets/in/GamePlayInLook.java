package fr.badblock.game.v1_8_R3.packets.in;

import fr.badblock.game.v1_8_R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInLook;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInLook;

@Getter public class GamePlayInLook extends GameBadblockInPacket implements PlayInLook {
	private float yaw;
	private float pitch;
	
	public GamePlayInLook(PacketPlayInLook packet){
		super(packet);
		this.yaw   = packet.d();
		this.pitch = packet.e();
	}
}

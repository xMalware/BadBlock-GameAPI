package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInPositionLook;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInPositionLook;

@Getter public class GamePlayInPositionLook extends GameBadblockInPacket implements PlayInPositionLook {
	private Vector3f position;
	private float 	 yaw;
	private float    pitch;

	public GamePlayInPositionLook(PacketPlayInPositionLook packet){
		super(packet);
		this.position = new Vector3f(packet.a(), packet.b(), packet.c());
		
		this.yaw      = packet.d();
		this.pitch    = packet.e();
	}
}

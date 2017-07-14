package fr.badblock.game.core18R3.packets.in;

import fr.badblock.gameapi.packets.in.play.PlayInPosition;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying.PacketPlayInPosition;

@Getter public class GamePlayInPosition extends GamePlayInFlying implements PlayInPosition {

	public GamePlayInPosition(PacketPlayInPosition packet){
		super(packet);
		this.position = new Vector3f(packet.a(), packet.b(), packet.c());
	}
}

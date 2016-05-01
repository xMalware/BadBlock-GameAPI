package fr.badblock.game.v1_8_R3.packets.in;

import fr.badblock.game.v1_8_R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;

public class GamePlayInArmAnimation extends GameBadblockInPacket implements PlayInArmAnimation {
	public GamePlayInArmAnimation(PacketPlayInArmAnimation packet){
		super(packet);
	}
}

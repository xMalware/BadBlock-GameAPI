package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInHeldItemSlot;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;

@Getter public class GamePlayInHeldItemSlot extends GameBadblockInPacket implements PlayInHeldItemSlot {
	private int slot;
	
	public GamePlayInHeldItemSlot(PacketPlayInHeldItemSlot packet){
		super(packet);
		this.slot = packet.a();
	}
}

package fr.badblock.game.core18R3.packets;

import fr.badblock.gameapi.packets.BadblockInPacket;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.Packet;

public abstract class GameBadblockInPacket extends GameBadblockPacket implements BadblockInPacket {

	@Setter
	Packet<?> packet;

	public GameBadblockInPacket(Packet<?> packet) {
		this.setPacket(packet);
	}

	public Packet<?> toNms() {
		return this.packet;
	}

}

package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInBlockDig;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;

@Getter
public class GamePlayInBlockDig extends GameBadblockInPacket implements PlayInBlockDig {
	private BlockDigAction action;
	private Vector3f position;
	private BlockFace blockFace;

	public GamePlayInBlockDig(PacketPlayInBlockDig packet) {
		super(packet);
		this.action = BlockDigAction.getById(packet.c().ordinal());
		this.position = new Vector3f(packet.a().getX(), packet.a().getY(), packet.a().getZ());
		this.blockFace = BlockFace.getById(packet.b().ordinal());
	}
}

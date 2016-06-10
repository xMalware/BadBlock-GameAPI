package fr.badblock.game.v1_8_R3.anticheat.listeners;

import fr.badblock.gameapi.packets.InPacketListener;
import fr.badblock.gameapi.packets.in.play.PlayInPositionLook;
import fr.badblock.gameapi.players.BadblockPlayer;

public class MoveRelListener extends InPacketListener<PlayInPositionLook> {
	@Override
	public void listen(BadblockPlayer player, PlayInPositionLook packet) {
		MoveUtils.move(player, packet.getPosition());
	}
}

package fr.badblock.game.v1_8_R3.anticheat.listeners;

import fr.badblock.gameapi.packets.InPacketListener;
import fr.badblock.gameapi.packets.in.play.PlayInPosition;
import fr.badblock.gameapi.players.BadblockPlayer;

public class MoveListener extends InPacketListener<PlayInPosition> {
	@Override
	public void listen(BadblockPlayer player, PlayInPosition packet) {
		MoveUtils.move(player, packet.getPosition());
	}
}

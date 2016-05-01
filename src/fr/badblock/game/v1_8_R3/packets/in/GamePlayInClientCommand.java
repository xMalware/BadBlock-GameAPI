package fr.badblock.game.v1_8_R3.packets.in;

import fr.badblock.game.v1_8_R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInClientCommand;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;

@Getter public class GamePlayInClientCommand extends GameBadblockInPacket implements PlayInClientCommand {
	private ClientCommands action;

	public GamePlayInClientCommand(PacketPlayInClientCommand packet){
		super(packet);
		ClientCommands.valueOf(packet.a().name());
	}
}

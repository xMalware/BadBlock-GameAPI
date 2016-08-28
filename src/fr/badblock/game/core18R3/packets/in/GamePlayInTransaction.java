package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInTransaction;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;

@Getter
public class GamePlayInTransaction extends GameBadblockInPacket implements PlayInTransaction {
	private int windowId;
	private short actionNumber;
	private boolean accepted;

	public GamePlayInTransaction(PacketPlayInTransaction packet) {
		super(packet);
		this.windowId = packet.a();
		this.actionNumber = packet.b();

		Reflector reflector = new Reflector(packet);
		try {
			this.accepted = (boolean) reflector.getFieldValue("c");
		} catch (Exception e) {
		}
	}
}

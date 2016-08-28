package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInEntityAction;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;

@Getter
public class GamePlayInEntityAction extends GameBadblockInPacket implements PlayInEntityAction {
	private int entityId;
	private EntityActions action;
	private int actionParameter;

	public GamePlayInEntityAction(PacketPlayInEntityAction packet) {
		super(packet);
		Reflector reflector = new Reflector(packet);
		try {
			this.entityId = (int) reflector.getFieldValue("a");
		} catch (Exception e) {
		}

		this.action = EntityActions.valueOf(packet.b().name());
		this.actionParameter = packet.c();
	}
}

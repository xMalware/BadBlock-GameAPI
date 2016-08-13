package fr.badblock.game.core18R3.packets.in;

import java.util.UUID;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInSpectate;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInSpectate;

@Getter public class GamePlayInSpectate extends GameBadblockInPacket implements PlayInSpectate {
	private UUID playerUID;
	
	public GamePlayInSpectate(PacketPlayInSpectate packet){
		super(packet);
		Reflector reflector = new Reflector(packet);
		try {
			this.playerUID = (UUID) reflector.getFieldValue("a");
		} catch (Exception e){}
	}
}

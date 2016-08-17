package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInCloseWindow;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;

@Getter public class GamePlayInCloseWindow extends GameBadblockInPacket implements PlayInCloseWindow {
	private int windowId;
	
	public GamePlayInCloseWindow(PacketPlayInCloseWindow packet){
		super(packet);
		Reflector reflector = new Reflector(packet);
		try {
			this.windowId = (int) reflector.getFieldValue("id");
		} catch (Exception e){}
	}
}

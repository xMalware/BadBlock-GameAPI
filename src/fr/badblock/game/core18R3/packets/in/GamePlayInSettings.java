package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInSettings;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInSettings;

@Getter public class GamePlayInSettings extends GameBadblockInPacket implements PlayInSettings {
	private String   locale;
	private int	     viewDistance;
	private ChatMode chatMode;
	private boolean  colorEnabled;
	private int 	 displayedSkinParts;
	
	public GamePlayInSettings(PacketPlayInSettings packet){
		super(packet);
		this.locale 			= packet.a();
		this.chatMode 			= ChatMode.getById(packet.c().a());
		this.colorEnabled 	    = packet.d();
		this.displayedSkinParts = packet.e();
		
		Reflector reflector = new Reflector(packet);
		try {
			this.viewDistance = (int) reflector.getFieldValue("b");
		} catch (Exception e){}
	}
}

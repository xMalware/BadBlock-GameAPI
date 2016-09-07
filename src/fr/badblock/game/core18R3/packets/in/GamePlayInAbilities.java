package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInAbilities;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInAbilities;

@Getter public class GamePlayInAbilities extends GameBadblockInPacket implements PlayInAbilities {
	private boolean	creative;
	private boolean flying;
	private boolean allowedToFly;
	private boolean godmoded;
	private float   flyingSpeed;
	private float   walkingSpeed;
	
	public GamePlayInAbilities(PacketPlayInAbilities packet){
		super(packet);
		this.creative = packet.a();
		this.flying	  = packet.isFlying();
		this.allowedToFly = packet.c();
		this.godmoded     = packet.d();
		
		Reflector reflector = new Reflector(packet);
		try {
			this.flyingSpeed  = (int) reflector.getFieldValue("e");
			this.walkingSpeed = (int) reflector.getFieldValue("f");
		} catch (Exception e){}
	}
}

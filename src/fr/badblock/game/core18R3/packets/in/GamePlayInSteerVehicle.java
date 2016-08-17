package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInSteerVehicle;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;

@Getter public class GamePlayInSteerVehicle extends GameBadblockInPacket implements PlayInSteerVehicle {
	private float   sideways;
	private float   forward;
	private boolean jump;
	private boolean unmount;
	
	public GamePlayInSteerVehicle(PacketPlayInSteerVehicle packet){
		super(packet);
		Reflector reflector = new Reflector(packet);
		try {
			this.sideways = (float)   reflector.getFieldValue("a");
			this.forward  = (float)   reflector.getFieldValue("b");
			this.jump     = (boolean) reflector.getFieldValue("c");
			this.unmount  = (boolean) reflector.getFieldValue("d");
		} catch (Exception e){}
	}
}

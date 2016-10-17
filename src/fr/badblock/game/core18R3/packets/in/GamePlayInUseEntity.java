package fr.badblock.game.core18R3.packets.in;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInUseEntity;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity.EnumEntityUseAction;

@Getter public class GamePlayInUseEntity extends GameBadblockInPacket implements PlayInUseEntity {
	@Setter
	private int 			entityId;
	@Setter
	private UseEntityAction action;
	private Vector3f 		targetPosition;
	
	public GamePlayInUseEntity(PacketPlayInUseEntity packet){
		super(packet);
		
		if(packet.b() == null){
			this.targetPosition = new Vector3f();
		} else this.targetPosition = new Vector3f(packet.b().a, packet.b().b, packet.b().c);
		
		this.action			= UseEntityAction.valueOf(packet.a().name());
		
		Reflector reflector = new Reflector(packet);
		try {
			this.entityId   = (int) reflector.getFieldValue("a");
		} catch (Exception e){}
	}

	@Override
	public Packet<?> toNms(){
		PacketPlayInUseEntity packet    = (PacketPlayInUseEntity) super.toNms();
		Reflector 			  reflector = new Reflector(packet);
		
		try {
			reflector.setFieldValue("a", entityId);
			reflector.setFieldValue("action", EnumEntityUseAction.valueOf( action.name() ));
		} catch (Exception e){}
		
		return new PacketPlayInUseEntity();
	}
}

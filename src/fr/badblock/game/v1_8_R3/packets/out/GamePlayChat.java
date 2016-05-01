package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayChat;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayChat extends GameBadblockOutPacket implements PlayChat {
	private ChatType 		type	   = ChatType.CHAT;
	private BaseComponent[] components = new BaseComponent[0];

	public GamePlayChat(PacketPlayOutChat packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			type  	   = ChatType.getByValue((byte) reflector.getFieldValue("b"));
			components = packet.components;
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutChat packet = new PacketPlayOutChat();
		packet.components = components;

		Reflector reflector = new Reflector(packet);
		reflector.setFieldValue("b", type.getValue());
		
		return packet;
	}

}

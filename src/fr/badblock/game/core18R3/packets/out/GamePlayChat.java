package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayChat;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayChat extends GameBadblockOutPacket implements PlayChat {
	private ChatType 		type	   = ChatType.CHAT;
	private String			content    = "";

	public GamePlayChat(PacketPlayOutChat packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			type  	   = ChatType.getByValue((byte) reflector.getFieldValue("b"));
			content	   = TextComponent.toLegacyText(fromChat((IChatBaseComponent) reflector.getFieldValue("a")));
		} catch(Exception e){}
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutChat(getChat(content), type.getValue());
	}

	@Override
	public BaseComponent[] getContentAsComponent() {
		return TextComponent.fromLegacyText(content);
	}
 
	@Override
	public PlayChat setContent(BaseComponent... components) {
		this.content = TextComponent.toLegacyText(components);
		return this;
	}

	@Override
	public PlayChat setContent(String content) {
		this.content = content;
		return this;
	}

}

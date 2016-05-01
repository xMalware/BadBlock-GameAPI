package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayPlayerListHeaderFooter;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayPlayerListHeaderFooter extends GameBadblockOutPacket implements PlayPlayerListHeaderFooter {
	private String header = "";
	private String footer = "";
	
	public GamePlayPlayerListHeaderFooter(PacketPlayOutPlayerListHeaderFooter packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			header = TextComponent.toLegacyText(fromChat((IChatBaseComponent) reflector.getFieldValue("a")));
			footer = TextComponent.toLegacyText(fromChat((IChatBaseComponent) reflector.getFieldValue("b")));
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(getChat(header));	
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("b", getChat(footer));
		
		return packet;
	}
}

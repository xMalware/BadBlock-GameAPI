package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayTitle;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayTitle extends GameBadblockOutPacket implements PlayTitle {
	private String content = "";
	private Action action  = Action.CLEAR;
	private long   fadeIn  = 0;
	private long   stay    = 0;
	private long   fadeOut = 0;
	
	public GamePlayTitle(PacketPlayOutTitle packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			content = TextComponent.toLegacyText(fromChat((IChatBaseComponent) reflector.getFieldValue("b")));
			action = Action.valueOf(((EnumTitleAction) reflector.getFieldValue("a")).name());
			fadeIn = (long) reflector.getFieldValue("c");
			fadeIn = (long) reflector.getFieldValue("d");
			fadeIn = (long) reflector.getFieldValue("e");
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutTitle(EnumTitleAction.valueOf(action.name()), getChat(content), (int) fadeIn, (int) stay, (int) fadeOut);
	}
}

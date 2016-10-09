package fr.badblock.game.core18R3.packets;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

public class GameBadblockPacket {
	
	@Getter@Setter private boolean 		  cancelled;

	public IChatBaseComponent getChat(String base){
		base = base.replace("\"", "\\\"");
		base = base.replace("\\", "\\\\");
		return ChatSerializer.a("{\"text\": \"" + base + "\"}");
	}
	
	public BaseComponent[] fromChat(IChatBaseComponent base){
		if(base == null) return TextComponent.fromLegacyText("");
		
		return ComponentSerializer.parse(ChatSerializer.a(base));
	}
	
	public String toLegacy(IChatBaseComponent base){
		return BaseComponent.toLegacyText(fromChat(base));
	}

}

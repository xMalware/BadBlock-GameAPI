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
		//return ChatSerializer.a(ComponentSerializer.toString(TextComponent.fromLegacyText(base)[0]));
		return ChatSerializer.a("{\"text\": \"" + base + "\"}");
	}
	
	public BaseComponent[] fromChat(IChatBaseComponent base){
		if(base == null) return TextComponent.fromLegacyText("");
		
		return ComponentSerializer.parse(ChatSerializer.a(base));
	}

}

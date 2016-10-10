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
		String res = "";
		
		boolean wasBackslash = false;
		
		for(int i=0;i<base.length();i++){
			if(wasBackslash){
				char car =  base.charAt(i);
				
				if(car == '"' || car == 'n' || car == 't'){
					res += "\\";					
				} else {
					res += "\\\\";
				}
				
				
				res += base.charAt(i);
				
				wasBackslash = false;
			} else if(base.charAt(i) == '\\'){
				wasBackslash = true;
			} else {
				if(base.charAt(i) == '"'){
					res += "\\\"";
				} else res += base.charAt(i);
			}
		}
		
		if(wasBackslash){
			res += "\\\\";
		}
		
		return ChatSerializer.a("{\"text\": \"" + res + "\"}");
	}
	
	public BaseComponent[] fromChat(IChatBaseComponent base){
		if(base == null) return TextComponent.fromLegacyText("");
		
		return ComponentSerializer.parse(ChatSerializer.a(base));
	}
	
	public String toLegacy(IChatBaseComponent base){
		return BaseComponent.toLegacyText(fromChat(base));
	}

}

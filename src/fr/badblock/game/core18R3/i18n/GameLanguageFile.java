package fr.badblock.game.core18R3.i18n;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.gameapi.utils.i18n.Message;

public class GameLanguageFile {
	private final File 			 	 file;
	private Map<String, GameMessage> content;
	
	public GameLanguageFile(File file, String whenUnknow){
		this.file    = file;
		this.content = Maps.newLinkedHashMap();
		
		JsonObject object = JsonUtils.loadObject(file);
		
		for(Entry<String, JsonElement> entry : object.entrySet()){
			if(entry.getValue().isJsonObject()){
				content.put(entry.getKey().toLowerCase(), JsonUtils.convert(entry.getValue(), GameMessage.class));
				content.get(entry.getKey().toLowerCase()).verify(whenUnknow);
			}
		}
	}
	
	public Message getMessage(String key, String def){
		key = key.toLowerCase();
		
		if(!content.containsKey(key)){
			content.put(key, new GameMessage(def));
		}
		
		return content.get(key);
	}
	
	public String getName(){
		return file.getName().split("\\.")[0].toLowerCase();
	}
	
	public void save(){
		JsonUtils.save(file, content, true);
	}
}

package fr.badblock.game.core18R3.i18n;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.general.ArraysUtils;
import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.Language;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.Message;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.i18n.TranslatableWord;
import fr.badblock.gameapi.utils.i18n.Word;
import fr.badblock.gameapi.utils.i18n.Word.WordDeterminant;
import lombok.Getter;

public class GameLanguage implements Language {
	@Getter private final Locale 							locale;
	private final		  File	 							configFile;
	private final		  File	 							textFolder;
	private final		  File	 							wordFolder;
	
	private final		  Map<String, GameMessage>		 	messages;
	private final		  Map<String, GameLanguageWordFile> wordFiles;
	private 		  	  GameLanguageConfig				config;
	
	public GameLanguage(Locale locale, File folder){
		if(!folder.exists()) folder.mkdirs();
		
		this.locale    = locale;
		this.messages  = Maps.newConcurrentMap();
		this.wordFiles = Maps.newConcurrentMap();
		
		configFile	   = new File(folder, "config.json");
		config		   = JsonUtils.load(configFile, GameLanguageConfig.class);
		
		textFolder = new File(folder, "texts");
		wordFolder = new File(folder, "words");
		
		if(config == null)
			config = new GameLanguageConfig();
		if(!textFolder.exists())
			textFolder.mkdirs();
		if(!wordFolder.exists())
			wordFolder.mkdirs();
		
		for(File file : wordFolder.listFiles()){
			if(!file.isDirectory()){
				GameLanguageWordFile language = new GameLanguageWordFile(file);
				wordFiles.put(language.getName(), language);
			}
		}
		
		for(EntityType type : EntityType.values()){
			getWord("entities." + type.name().toLowerCase());
		}
		
		for(Material material : Material.values()){
			getWord("materials." + material.name().toLowerCase());
		}
		
		for(PotionEffectType type : PotionEffectType.values()){
			if(type != null){
				getWord("potions." + type.getName().toLowerCase());
			}
		}
		
		for(DyeColor color : DyeColor.values()){
			getWord("dyecolors." + color.name().toLowerCase());
		}
		
		for(DamageCause damageCause : DamageCause.values()){
			get("deathmessages.pvp." + damageCause.name().toLowerCase());
			get("deathmessages.pve." + damageCause.name().toLowerCase());
			get("deathmessages.normal." + damageCause.name().toLowerCase());
		}
	}
	
	@Override
	public String[] get(String key, Object... args) {
		return formatMessage(getMessage(key), args);
	}

	@Override
	public String getWord(String key, boolean plural, WordDeterminant determinant) {
		return getWord(key).get(plural, determinant);
	}
	
	@Override
	public Message getMessage(String key) {
		if(key.isEmpty())
			throw new IllegalArgumentException("Empty key given");
		
		key = key.toLowerCase();
		
		if(messages.containsKey(key))
			return messages.get(key);
		
		String[] splitted     = key.split("\\.");
		
		File file = textFolder;
		
		for(int i=0;i<splitted.length-1;i++){
			file = new File(file, splitted[i]);
			
			if(!file.exists())
				file.mkdirs();
		}
		
		file = new File(file, splitted[splitted.length - 1] + ".json");
		
		GameMessage message = null;
		
		if(!file.exists()){
			message = new GameMessage(ChatColor.RED + key);
			JsonUtils.save(file, message, true);
		} else {
			message = JsonUtils.load(file, GameMessage.class);
			message.verify(ChatColor.RED + key);
		}
		
		message.file = file;
		messages.put(key, message);
		
		/*GameLanguageFile file = getLanguageFile(splitted[0].toLowerCase());

		if(file == null){
			file = new GameLanguageFile(new File(textFolder, splitted[0].toLowerCase() + ".json"), getMessageWhenUnknow());
			files.put(splitted[0].toLowerCase(), file);
		}*/
		
		// key = StringUtils.join(splitted, ".", 1);
		
		return message;
	}
	
	@Override
	public Word getWord(String key) {
		String[] splitted     = key.split("\\.");
		GameLanguageWordFile file = getLanguageWordFile(splitted[0].toLowerCase());

		if(file == null){
			file = new GameLanguageWordFile(new File(wordFolder, splitted[0].toLowerCase() + ".json"));
			wordFiles.put(splitted[0].toLowerCase(), file);
		}
		
		key = StringUtils.join(splitted, ".", 1);
		
		return file.getWord(key);
	}
	
	/*protected GameLanguageFile getLanguageFile(String file){
		file = file.toLowerCase();
		
		if(!files.containsKey(file)){
			return files.put(file, new GameLanguageFile(new File(textFolder, file + ".json"), getMessageWhenUnknow()));
		} else {
			return files.get(file);
		}
	}*/
	
	protected GameLanguageWordFile getLanguageWordFile(String file){
		file = file.toLowerCase();
		
		if(!wordFiles.containsKey(file)){
			return wordFiles.put(file, new GameLanguageWordFile(new File(wordFolder, file + ".json")));
		} else {
			return wordFiles.get(file);
		}
	}

	@Override
	public String[] formatMessage(Message message, Object... args) {
		String[]     base   = message.getUnformattedMessage();
		List<String> result = new ArrayList<>();
		
		if(message.useHeader()){
			result.addAll(Arrays.asList(getHeader()));
		} else if(message.useShortHeader()){
			base[0] = getShortHeader() + base[0];
		}
		
		result.addAll(Arrays.asList(base));
		
		if(message.useFooter())
			result.add(getFooter());
		
		String[] arrayResult = result.toArray(new String[0]);
		
		for(int i=0;i<args.length;i++) {
			String toString = toString(args[i]);
			arrayResult = ArraysUtils.replace(arrayResult, "%" + i, toString);
		}
		
		return GameAPI.i18n().replaceColors(arrayResult);
	}
	
	private String toString(Object object){
		if(object == null)
			return "null";
		else if(object instanceof TranslatableWord){
			return ((TranslatableWord) object).getWord(getLocale());
		} else if(object instanceof TranslatableString){
			return get(((TranslatableString) object).getKey())[0];
		} else return object.toString();
	}

	@Override
	public String[] getHeader() {
		return config.header;
	}

	@Override
	public String getFooter() {
		return config.footer;
	}

	@Override
	public String getShortHeader() {
		return config.shortHeader;
	}
	
	public void save(){
		JsonUtils.save(configFile, config, true);
		
		for(GameMessage message : messages.values()){
			JsonUtils.save(message.file, message, true);
		}
		
		for(GameLanguageWordFile file : wordFiles.values()){
			file.save();
		}
	}
}

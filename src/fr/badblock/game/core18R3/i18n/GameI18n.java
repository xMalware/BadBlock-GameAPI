package fr.badblock.game.core18R3.i18n;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.I18n;
import fr.badblock.gameapi.utils.i18n.Language;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.Word.WordDeterminant;

public class GameI18n implements I18n {
	private final static Locale def = Locale.FRENCH_FRANCE;
	private Map<Locale, GameLanguage> languages;

	public GameI18n() {

	}

	/*
	 * private void copy(File folderFrom, File folderTo) throws IOException {
	 * if(!folderTo.exists()) folderTo.mkdirs();
	 * 
	 * for(File file : folderFrom.listFiles()){ File to = new File(folderTo,
	 * file.getName());
	 * 
	 * if(file.isDirectory()) copy(file, to); else Files.copy(file, to); } }
	 */

	@Override
	public void broadcast(String key, Object... args) {
		for (Player player : Bukkit.getOnlinePlayers())
			sendMessage(player, key, args);
	}

	@Override
	public String[] get(CommandSender sender, String key, Object... args) {
		return get(getLanguage(sender), key, args);
	}

	@Override
	public String[] get(Locale locale, String key, Object... args) {
		if (locale == null)
			locale = def;

		Language language = languages.get(locale);

		if (language == null)
			throw new RuntimeException("Trying to access to an unconfigurated language !");

		return language.get(key, args);
	}

	@Override
	public String[] get(String key, Object... args) {
		return get(def, key, args);
	}

	@Override
	public Collection<Locale> getConfiguratedLocales() {
		return Collections.unmodifiableCollection(languages.keySet());
	}

	protected Locale getLanguage(CommandSender sender) {
		Locale locale = null;

		if (sender instanceof BadblockPlayer) {
			BadblockPlayer player = (BadblockPlayer) sender;
			locale = player.getPlayerData().getLocale();
		} else {
			locale = Locale.ENGLISH_US;
		}

		if (locale == null)
			locale = def;
		return locale;
	}

	@Override
	public Language getLanguage(Locale locale) {
		return languages.get(locale);
	}

	@Override
	public String getWord(Locale locale, String key, boolean plural, WordDeterminant determinant) {
		if (locale == null)
			locale = def;

		Language language = languages.get(locale);

		if (language == null)
			throw new RuntimeException("Trying to access to an unconfigurated language !");

		return language.getWord(key, plural, determinant);
	}

	@Override
	public String getWord(String key, boolean plural, WordDeterminant determinant) {
		return getWord(def, key, plural, determinant);
	}

	public void load(File folder) {
		/*
		 * File i18nFile = new File("/home/dev01/i18n");
		 * 
		 * if(!i18nFile.exists()) i18nFile.mkdirs();
		 * 
		 * try { Runtime.getRuntime().exec("rm -rf " +
		 * folder.getAbsolutePath()).waitFor(); copy(i18nFile, folder); }
		 * catch(Exception e){ e.printStackTrace(); }
		 */
		if (!folder.exists())
			folder.mkdirs();

		languages = Maps.newConcurrentMap();

		for (File languageFolder : folder.listFiles()) {
			if (languageFolder.isDirectory()) {
				Locale locale = Locale.getLocale(languageFolder.getName());

				if (locale == null) {
					GameAPI.logWarning("Invalid language folder (" + languageFolder.getName() + ") : unknow language");
				} else {
					GameAPI.logColor("&b[GameAPI] &aLecture i18n ... (" + locale + ")");

					languages.put(locale, new GameLanguage(locale, languageFolder));
				}
			}
		}

		Locale def = Locale.FRENCH_FRANCE;

		if (!languages.containsKey(def)) {
			languages.put(def, new GameLanguage(def, new File(folder, def.getLocaleId())));
		}

		def = Locale.ENGLISH_US;

		if (!languages.containsKey(def)) {
			languages.put(def, new GameLanguage(def, new File(folder, def.getLocaleId())));
		}
	}

	@Override
	public List<String> replaceColors(List<String> base) {
		List<String> result = new ArrayList<>();

		for (String line : base)
			result.add(replaceColors(line));

		return result;
	}

	@Override
	public String replaceColors(String base) {
		return ChatColor.translateAlternateColorCodes('&', base);
	}

	@Override
	public String[] replaceColors(String... base) {
		for (int i = 0; i < base.length; i++) {
			base[i] = replaceColors(base[i]);
		}

		return base;
	}

	public void save() {
		for (GameLanguage language : languages.values()) {
			language.save();
		}
	}

	@Override
	public void sendMessage(CommandSender sender, String key, Object... args) {
		String[] messages = get(sender, key, args);
		for (String message : messages)
			sender.sendMessage(message);
	}

}

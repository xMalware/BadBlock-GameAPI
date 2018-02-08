package fr.badblock.game.core18R3.listeners;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.badblock.game.core18R3.players.data.ChatData;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatListener extends BadListener {
	public static boolean enabled = false;
	public static boolean team	  = false;
	public static String  custom  = null;
	public static Map<Integer, ChatData> messages = new HashMap<>();

	private Set<String> gg = new HashSet<>();

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onChat(AsyncPlayerChatEvent e){
		if(!enabled) return;

		e.setCancelled(true);

		BadblockPlayer player = (BadblockPlayer) e.getPlayer();

		protectColor(player, e);

		if (player.getBadblockMode().equals(BadblockMode.PLAYER))
		{
			GameState gameState = GameAPI.getAPI().getGameServer().getGameState();
			if (gameState.equals(GameState.FINISHED) || gameState.equals(GameState.STOPPING))
			{
				String message = e.getMessage();
				if (message.toLowerCase().startsWith("gg ")
						|| message.toLowerCase().equalsIgnoreCase("gg")
						|| message.toLowerCase().equalsIgnoreCase("gg.")
						|| message.toLowerCase().equalsIgnoreCase("gg!"))
				{	
					String lowerName = player.getName().toLowerCase();
					if (gg.contains(lowerName))
					{
						e.setCancelled(true);
						player.sendTranslatedMessage("game.gg.youalreadysaythat");
						return;
					}
					int addedBadcoins = player.getPlayerData().addBadcoins(3, true);
					long addedXp = player.getPlayerData().addXp(3, true);
					player.saveGameData();
					player.sendTranslatedMessage("game.gg.win", addedBadcoins, addedXp);
					gg.add(lowerName);
				}
			}
		}

		if(player.getBadblockMode() == BadblockMode.SPECTATOR){
			int points = player.getRanked() == null ? 0 : player.getRanked().getPoints();
			TranslatableString result = new TranslatableString("chat.spectator" + (custom == null ? "" : "." + custom), (LoginListener.l.contains(player.getName()) ? "§4§l❤ §r" : "") + player.getName(), player.getGroupPrefix(), e.getMessage(), points, player.getGroupSuffix());
			int i = new Random().nextInt(Integer.MAX_VALUE);
			while (messages.containsKey(i)) {
				i = new Random().nextInt(Integer.MAX_VALUE);
			}
			messages.put(i, new ChatData(player.getName(), e.getMessage()));
			TextComponent message = new TextComponent( GameAPI.i18n().get("chat.report_icon")[0] );
			message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/creport " + i) );
			message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("chat.report_hover", player.getName())[0]).create() ) );

			TextComponent message2 = new TextComponent( GameAPI.i18n().get("chat.bab_icon")[0] );
			message2.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/bab " + player.getName()) );
			message2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("chat.bab_hover", player.getName())[0]).create() ) );

			for(Player p : e.getRecipients()){
				BadblockPlayer bPlayer = (BadblockPlayer) p;
				// Ignore
				if (bPlayer.getPlayerData().getIgnoreList() != null)
				{
					boolean containsSearchStr = bPlayer.getPlayerData().getIgnoreList().stream().filter(s -> 
					s.equalsIgnoreCase(player.getName())).findFirst().isPresent();
					if (containsSearchStr)
					{
						continue;
					}
				}
				TextComponent textComponent = new TextComponent();
				String text = result.get((BadblockPlayer) p)[0];
				String coloredResult = "";
				String base = "";
				String pb = getLastColors(text);
				// Messages colorisés (mentions)
				if (!p.getName().equalsIgnoreCase(player.getName()))
				{
					if (text.toLowerCase().contains(p.getName().toLowerCase()))
					{
						text = text.replaceAll("(?i)" + p.getName().toLowerCase(), ChatColor.AQUA + "" + ChatColor.BOLD + p.getName() + pb);
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 100, 1);
					}
				}
				int length = text.length();
				boolean wasColor = true;
				for (int index = 0; index < length; index++){
					char character = text.charAt(index);
					boolean b = false;
					if (character == '&' || character == '§') {
						wasColor = true;
						b = true;
					}
					base += character;
					if (!wasColor) coloredResult += getLastColors(base);
					coloredResult += character;
					if (!b)
						wasColor = false;
				}
				textComponent.setText(" " + coloredResult);
				if (player.hasPermission(GamePermission.MODERATOR))
					p.sendMessage(message, message2, textComponent);
				else if(bPlayer.getBadblockMode() == BadblockMode.SPECTATOR)
					p.sendMessage(message, textComponent);
			}
		} else if(team && player.getTeam() != null && (
				e.getMessage().charAt(0) == '!'
				|| e.getMessage().charAt(0) == '$'
				|| e.getMessage().charAt(0) == '@')
				){
			player.performCommand("team " + e.getMessage().substring(1, e.getMessage().length() - 1));
		} else {
			Object team = "";

			if(player.getTeam() != null)
				team = player.getTeam().getChatPrefix();

			int i = new Random().nextInt(Integer.MAX_VALUE);
			while (messages.containsKey(i)) {
				i = new Random().nextInt(Integer.MAX_VALUE);
			}
			messages.put(i, new ChatData(player.getName(), e.getMessage()));
			int points = player.getRanked() == null ? 0 : player.getRanked().getPoints();
			TranslatableString s = new TranslatableString("chat.player" + (custom == null ? "" : "." + custom), (LoginListener.l.contains(player.getName()) ? "§4§l❤ §c" : "") + player.getName(), player.getGroupPrefix(), team, e.getMessage(), points, player.getGroupSuffix());
			TextComponent message = new TextComponent( GameAPI.i18n().get("chat.report_icon")[0] );
			message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/creport " + i) );
			message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("chat.report_hover", player.getName())[0]).create() ) );

			TextComponent message2 = new TextComponent( GameAPI.i18n().get("chat.bab_icon")[0] );
			message2.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/bab " + player.getName()) );
			message2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("chat.bab_hover", player.getName())[0]).create() ) );

			for(Player pl : e.getRecipients()) {
				TextComponent textComponent = new TextComponent();
				BadblockPlayer blp = (BadblockPlayer) pl;
				// Ignore
				if (blp.getPlayerData().getIgnoreList() != null)
				{
					boolean containsSearchStr = blp.getPlayerData().getIgnoreList().stream().filter(s2 -> 
					s2.equalsIgnoreCase(player.getName())).findFirst().isPresent();
					if (containsSearchStr)
					{
						continue;
					}
				}
				String text = s.get(blp)[0];
				String coloredResult = "";
				String base = "";
				String p = getLastColors(text);
				// Messages colorisés (mentions)
				if (!pl.getName().equalsIgnoreCase(player.getName()))
				{
					if (text.toLowerCase().contains(pl.getName().toLowerCase()))
					{
						text = text.replaceAll("(?i)" + pl.getName().toLowerCase(), ChatColor.AQUA + "" + ChatColor.BOLD + pl.getName() + p);
						pl.playSound(pl.getLocation(), Sound.ORB_PICKUP, 100, 1);
					}
				}
				int length = text.length();
				boolean wasColor = true;
				for (int index = 0; index < length; index++){
					char character = text.charAt(index);
					boolean b = false;
					if (character == '&' || character == '§') {
						wasColor = true;
						b = true;
					}
					base += character;
					if (!wasColor) coloredResult += getLastColors(base);
					coloredResult += character;
					if (!b)
						wasColor = false;
				}
				textComponent.setText(" " + coloredResult);
				if (blp.hasPermission(GamePermission.MODERATOR))
					blp.sendMessage(message, message2, textComponent);
				else
					blp.sendMessage(message, textComponent);
			}
		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e){
		if(!enabled) return;
		BadblockPlayer player = (BadblockPlayer) e.getPlayer();
		if((e.getMessage().startsWith("/t ") || e.getMessage().equalsIgnoreCase("/t") || e.getMessage().startsWith("/team ") || e.getMessage().equalsIgnoreCase("/team"))){
			e.setCancelled(true);
			if (!team) {
				player.sendTranslatedMessage("game.doesntexisthaveteams");
			}else if (player.getTeam() == null) {
				player.sendTranslatedMessage("game.youdonthaveteam");
			}else{
				if(!e.getMessage().contains(" ")) {
					player.sendTranslatedMessage("game.pleasespecifyamessage");
				}else{
					int i = new Random().nextInt(Integer.MAX_VALUE);
					while (messages.containsKey(i)) {
						i = new Random().nextInt(Integer.MAX_VALUE);
					}
					messages.put(i, new ChatData(player.getName(), e.getMessage()));
					TextComponent message = new TextComponent( GameAPI.i18n().get("chat.report_icon")[0] );
					message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/creport " + i) );
					message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("chat.report_hover", player.getName())[0]).create() ) );

					TextComponent message2 = new TextComponent( GameAPI.i18n().get("chat.bab_icon")[0] );
					message2.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/bab " + player.getName()) );
					message2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("chat.bab_hover", player.getName())[0]).create() ) );

					int points = player.getRanked() == null ? 0 : player.getRanked().getPoints();
					TranslatableString result = new TranslatableString("chat.team" + (custom == null ? "" : "." + custom), (LoginListener.l.contains(player.getName()) ? "§4§l❤ §c" : "") + player.getName(), player.getGroupPrefix(), player.getTeam().getChatName(), e.getMessage().replace(e.getMessage().split(" ")[0], ""), points);
					for(BadblockPlayer p : player.getTeam().getOnlinePlayers()){
						// Ignore
						if (p.getPlayerData().getIgnoreList() != null)
						{
							boolean containsSearchStr = p.getPlayerData().getIgnoreList().stream().filter(s2 -> 
							s2.equalsIgnoreCase(player.getName())).findFirst().isPresent();
							if (containsSearchStr)
							{
								continue;
							}
						}
						TextComponent textComponent = new TextComponent();
						String text = result.get((BadblockPlayer) p)[0];
						String coloredResult = "";
						String base = "";
						String bc = getLastColors(text);
						// Messages colorisés (mentions)
						if (!p.getName().equalsIgnoreCase(player.getName()))
						{
							p.playSound(p.getLocation(), Sound.ORB_PICKUP, 100, 1);
							if (text.toLowerCase().contains(p.getName().toLowerCase()))
							{
								text = text.replaceAll("(?i)" + p.getName().toLowerCase(), ChatColor.AQUA + "" + ChatColor.BOLD +
										p.getName() + bc);
							}
						}
						int length = text.length();
						boolean wasColor = true;
						for (int index = 0; index < length; index++){
							char character = text.charAt(index);
							boolean b = false;
							if (character == '&' || character == '§') {
								wasColor = true;
								b = true;
							}
							base += character;
							if (!wasColor) coloredResult += getLastColors(base);
							coloredResult += character;
							if (!b)
								wasColor = false;
						}
						textComponent.setText(" " + coloredResult);
						if (e.getRecipients().contains(p))
							if (p.hasPermission(GamePermission.MODERATOR))
							{
								p.sendMessage(message, message2, textComponent);
							}
							else
							{
								p.sendMessage(message, textComponent);
							}
					}
				}
			}
		}
	}

	public static String getLastColors(String input){
		String result = "";
		int length = input.length();
		for (int index = length - 1; index > -1; index--){
			char section = input.charAt(index);
			if ((section == '§' || section == '&') && (index < length - 1)){
				char c = input.charAt(index + 1);
				ChatColor color = ChatColor.getByChar(c);
				if (color != null) {
					result = color.toString() + result;
					if ((color.equals(ChatColor.RESET))){
						break;
					}
				}
			}
		}
		return result;
	}

	public static void protectColor(BadblockPlayer player, AsyncPlayerChatEvent event) {
		if(player.hasPermission(GamePermission.ADMIN)){
			String temp = ChatColor.translateAlternateColorCodes('&', event.getMessage());
			event.setMessage(temp);
		}else event.setMessage(event.getMessage().replace("&", ""));
	}

}

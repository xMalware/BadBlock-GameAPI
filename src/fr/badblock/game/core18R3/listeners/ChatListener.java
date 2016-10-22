package fr.badblock.game.core18R3.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
import fr.badblock.gameapi.utils.i18n.TranslatableString;

public class ChatListener extends BadListener {
	public static boolean enabled = false;
	public static boolean team	  = false;
	public static String  custom  = null;

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onChat(AsyncPlayerChatEvent e){
		if(!enabled) return;

		e.setCancelled(true);

		BadblockPlayer player = (BadblockPlayer) e.getPlayer();


		if(player.getBadblockMode() == BadblockMode.SPECTATOR){
			TranslatableString result = new TranslatableString("chat.spectator" + (custom == null ? "" : "." + custom), player.getName(), player.getGroupPrefix(), e.getMessage(), player.getPlayerData().getLevel());

			for(Player p : e.getRecipients()){
				BadblockPlayer bPlayer = (BadblockPlayer) p;

				if(bPlayer.getBadblockMode() == BadblockMode.SPECTATOR){
					result.send(bPlayer);
				}
			}
		} else if(team && player.getTeam() != null && e.getMessage().startsWith("$")){
			TranslatableString result = new TranslatableString("chat.team" + (custom == null ? "" : "." + custom), player.getName(), player.getGroupPrefix(), player.getTeam().getChatName(), e.getMessage().substring(1), player.getPlayerData().getLevel());

			for(BadblockPlayer p : player.getTeam().getOnlinePlayers()){
				if (e.getRecipients().contains(p))
					result.send(p);
			}
		} else {
			Object team = "";

			if(player.getTeam() != null)
				team = player.getTeam().getChatPrefix();

			TranslatableString s = new TranslatableString("chat.player" + (custom == null ? "" : "." + custom), player.getName(), player.getGroupPrefix(), team, e.getMessage(), player.getPlayerData().getLevel(), player.getGroupSuffix());
			for(Player pl : e.getRecipients())
				s.send(pl);
		}

	}
	
	public static void protectColor(BadblockPlayer player, AsyncPlayerChatEvent event) {
		if(!player.hasPermission(GamePermission.ADMIN)){

			String temp = ChatColor.translateAlternateColorCodes('&', event.getMessage());
			temp		= ChatColor.stripColor(temp);

			String res = "";
			
			for(int i=0;i<temp.length();i++){
				if(temp.charAt(i) == ChatColor.COLOR_CHAR){
					i++;
				} else res += temp.charAt(i);
			}
			
			event.setMessage(res);

		}
	}
	
}

package fr.badblock.bukkit.hub.listeners.players;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.badblock.bukkit.hub.listeners._HubListener;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.bukkit.hub.objects.HubStoredPlayer;
import fr.badblock.game.core18R3.listeners.ChatListener;
import fr.badblock.gameapi.players.BadblockPlayer;

public class AsyncPlayerChatListener extends _HubListener {

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		BadblockPlayer pl = (BadblockPlayer) event.getPlayer();
		HubStoredPlayer hubPlayer = HubStoredPlayer.get(pl);
		ChatListener.protectColor(pl, event);
		if (!hubPlayer.isHubChat()) {
			event.setCancelled(true);
			pl.sendTranslatedMessage("hub.disabledchat");
			return;
		}
		HubPlayer ho = HubPlayer.get(pl);
		if (!ho.canChat(pl.hasPermission("hub.vipchattimebypass"))) {
			event.setCancelled(true);
			pl.sendTranslatedMessage("hub.pleasewaitbetweeneachmessage_notvip");
			return;
		}
		if (ho.getMessage() != null) {
			if (ho.getMessage().equalsIgnoreCase(event.getMessage().replace(" ", ""))) {
				event.setCancelled(true);
				pl.sendTranslatedMessage("hub.dontrepeatyourself");
				return;
			}
		}
		ho.setMessage(event.getMessage().replaceAll(" ", ""));
		if (event.getPlayer().hasPermission("hub.bypassdisabledchat"))
			return;
		Iterator<Player> iterator = event.getRecipients().iterator();
		while (iterator.hasNext()) {
			BadblockPlayer player = (BadblockPlayer) iterator.next();
			HubStoredPlayer hubP = HubStoredPlayer.get(player);
			if (!hubP.isHubChat()) {
				iterator.remove();
			}
		}
	}

}

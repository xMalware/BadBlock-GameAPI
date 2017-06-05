package fr.badblock.game.core18R3.listeners;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.players.BadblockPlayer;

public class BowSpamListener extends BadListener {

	static Map<String, Long> shoot = new HashMap<>();
	static Map<String, Long> alert = new HashMap<>();
	
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void onBowSpam(EntityShootBowEvent e){
		if (!(e.getEntity() instanceof Player)) return;
		Player player = (Player) e.getEntity();
		long antiBowSpam = GamePlugin.getInstance().getAntiBowSpam();
		if (antiBowSpam == 0) return;
		String name = player.getName().toLowerCase();
		long time = shoot.containsKey(name) ? shoot.get(name) : 0;
		long current = System.currentTimeMillis();
		if (time < current) {
			long expire = current + antiBowSpam;
			shoot.put(name, expire);
		}else{
			e.setCancelled(true);
			double o = (time - current);
			double s = o / 1000.0D;
			String f = String.format("%.2f", s);
			BadblockPlayer badblockPlayer = (BadblockPlayer) player;
			long t = alert.containsKey(name) ? alert.get(name) : 0;
			if (t < current) {
				badblockPlayer.sendTranslatedMessage("game.bowspam", f);		
				alert.put(name, current + 500);
			}
		}
	}
		
}

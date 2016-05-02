package fr.badblock.game.v1_8_R3.listeners;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import fr.badblock.game.v1_8_R3.players.GameBadblockPlayer;
import fr.badblock.game.v1_8_R3.players.GameOfflinePlayer;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import net.minecraft.server.v1_8_R3.EntityPlayer;

/**
 * Listener servant à remplacé la classe CraftPlayer par GameBadblockPlayer et à demander à Ladder les informations joueur.
 * @author LeLanN
 */
public class LoginListener extends BadListener {
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent e){
		Reflector reflector = new Reflector(ReflectionUtils.getHandle(e.getPlayer()));
		
		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getUniqueId());
		GameBadblockPlayer player;

		try {
			player = new GameBadblockPlayer((CraftServer) Bukkit.getServer(), (EntityPlayer) reflector.getReflected(), (GameOfflinePlayer) offlinePlayer);
			reflector.setFieldValue("bukkitEntity", player);
		} catch (Exception exception) {
			System.out.println("Impossible de modifier la classe du joueur : ");
			exception.printStackTrace();
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		GameBadblockPlayer p = (GameBadblockPlayer) e.getPlayer();
		
		p.loadInjector();
		p.setHasJoined(true);
	}
}

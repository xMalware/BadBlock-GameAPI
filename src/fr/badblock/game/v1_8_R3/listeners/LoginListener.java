package fr.badblock.game.v1_8_R3.listeners;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.game.v1_8_R3.players.CommandInGameData;
import fr.badblock.game.v1_8_R3.players.GameBadblockPlayer;
import fr.badblock.game.v1_8_R3.players.GameOfflinePlayer;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.api.SpectatorJoinEvent;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.BadblockPlayer.GamePermission;
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
		if(GameAPI.getAPI().getWhitelistStatus() && !GameAPI.getAPI().isWhitelisted(e.getPlayer().getName())){
			e.setResult(Result.KICK_WHITELIST); return;
		}
		
		Reflector 			  reflector 	= new Reflector(ReflectionUtils.getHandle(e.getPlayer()));
		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getName());
		GameBadblockPlayer    player;

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

		if(GamePlugin.EMPTY_VERSION) return;
		
		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getName());

		if(offlinePlayer != null){
			p.changePlayerDimension(offlinePlayer.getFalseDimension());
			p.showCustomObjective(offlinePlayer.getCustomObjective());
			
			GamePlugin.getInstance().getGameServer().getPlayers().remove(offlinePlayer.getName().toLowerCase());
			GamePlugin.getInstance().getGameServer().getSavedPlayers().remove(offlinePlayer.getName().toLowerCase());
		} else if(GameAPI.getAPI().getGameServer().getGameState() != GameState.WAITING){
			Bukkit.getPluginManager().callEvent(new SpectatorJoinEvent(p));
			p.setBadblockMode(BadblockMode.SPECTATOR);
		}
		
		new BukkitRunnable(){
			@Override
			public void run(){
				
				for(Player player : Bukkit.getOnlinePlayers()){
					GameBadblockPlayer bp = (GameBadblockPlayer) player;
					if(bp.isDisguised()){
						bp.getDisguiseEntity().show(p);
					} else if(bp.inGameData(CommandInGameData.class).vanish && !p.hasPermission(GamePermission.BMODERATOR)){
						p.hidePlayer(bp);
					}
				}
				
			}
		}.runTaskLater(GameAPI.getAPI(), 10L);
	}
}

package fr.badblock.game.core18R3.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.players.ingamedata.GameOfflinePlayer;
import fr.badblock.game.core18R3.technologies.rabbitlisteners.VanishTeleportListener;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.events.PlayerGameInitEvent;
import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent.JoinReason;
import fr.badblock.gameapi.events.api.SpectatorJoinEvent;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.servers.JoinItems;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.threading.TaskManager;
import net.minecraft.server.v1_8_R3.EntityPlayer;

/**
 * Listener servant � remplac� la classe CraftPlayer par GameBadblockPlayer et � demander � Ladder les informations joueur.
 * @author LeLanN
 */
public class LoginListener extends BadListener {
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent e){
		if (GameAPI.getAPI().getRunType().equals(RunType.GAME)) {
			if (e.getResult().equals(Result.KICK_FULL) || BukkitUtils.getPlayers().size() >= Bukkit.getMaxPlayers()) {
				e.disallow(Result.KICK_FULL, "§cCe serveur est plein.");
			}
		}
		if (!GameAPI.isJoinable()) {
			e.disallow(Result.KICK_FULL, "§cCette partie est en cours.");
		}
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
	public void onInventoryClose(InventoryCloseEvent event) {
		BadblockPlayer player = (BadblockPlayer) event.getPlayer();
		if (player.getBadblockMode().equals(BadblockMode.SPECTATOR)) {
			if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning()) {
				Inventory inventory = event.getInventory();
				if (inventory != null && inventory.getName() != null && inventory.getName().equals(GameAPI.i18n().get(player.getPlayerData().getLocale(), "joinitems.kit.inventoryName")[0])) {
					manageRunningJoin(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		GameBadblockPlayer p = (GameBadblockPlayer) e.getPlayer();

		p.loadInjector();
		p.setHasJoined(true);

		if(GamePlugin.EMPTY_VERSION) return;

		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getName());

		if (VanishTeleportListener.time.containsKey(p.getName().toLowerCase()) && VanishTeleportListener.time.get(p.getName().toLowerCase()) > System.currentTimeMillis()) {
			VanishTeleportListener.manage(p, VanishTeleportListener.splitters.get(p.getName().toLowerCase()));
		}else if(offlinePlayer != null){
			p.changePlayerDimension(offlinePlayer.getFalseDimension());
			p.showCustomObjective(offlinePlayer.getCustomObjective());

			GamePlugin.getInstance().getGameServer().getPlayers().remove(offlinePlayer.getName().toLowerCase());
			GamePlugin.getInstance().getGameServer().getSavedPlayers().remove(offlinePlayer.getName().toLowerCase());

		} else if(GameAPI.getAPI().getGameServer().getGameState() == GameState.RUNNING){
			p.setVisible(false, player -> !player.getBadblockMode().equals(BadblockMode.SPECTATOR));
			Bukkit.getPluginManager().callEvent(new SpectatorJoinEvent(p));
			p.setBadblockMode(BadblockMode.SPECTATOR);
			p.sendTranslatedMessage("game.closetheinventorytoplaywiththedefaultkit");
			if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning() && BukkitUtils.getPlayers().size() <= Bukkit.getMaxPlayers()) {
				TaskManager.runTaskLater(new Runnable() {
					@Override
					public void run() {
						JoinItems joinItems = GameAPI.getAPI().getJoinItems();
						if (joinItems.getKits().isEmpty()) {
							// Manage
							manageRunningJoin(p);
						}
						CustomInventory inventory = GameAPI.getAPI().createCustomInventory(joinItems.getKits().size() / 9, GameAPI.i18n().get(p.getPlayerData().getLocale(), "joinitems.kit.inventoryName")[0]);

						int slot = 0;

						for(PlayerKit kit : joinItems.getKits()){
							if(kit != null){
								inventory.addClickableItem(slot, kit.getKitItem(p));
							}
							slot++;
						}

						inventory.openInventory(p);
						TaskManager.runTaskLater(new Runnable() {
							@Override
							public void run() {
								if (!p.isOnline()) return;
								if (p.getBadblockMode().equals(BadblockMode.SPECTATOR)) {
									p.closeInventory();
								}
							}
						}, 20 * 15);
					}
				}, 1);
			}
		} else if(GameAPI.getAPI().getGameServer().getGameState() == GameState.FINISHED){
			p.setVisible(false, player -> !player.getBadblockMode().equals(BadblockMode.SPECTATOR));
			Bukkit.getPluginManager().callEvent(new SpectatorJoinEvent(p));
			p.setBadblockMode(BadblockMode.SPECTATOR);
		}
		if (GamePlugin.getInstance().getGameServerManager().getRankedConfig().isRanked()) {
			GamePlugin.getAPI().getSqlDatabase().call("SELECT COUNT(*) AS count FROM rankeds WHERE playerName = '" + p.getName() + "'", SQLRequestType.QUERY, new Callback<ResultSet>() {

				@Override
				public void done(ResultSet result, Throwable error) {
					try {
						result.next();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					int count;
					try {
						count = result.getInt("count");
						if (count == 0) {
							GamePlugin.getAPI().getSqlDatabase().call("INSERT INTO rankeds(playerName) VALUES('" + p.getName() + "')", SQLRequestType.UPDATE);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
		}

		new BukkitRunnable(){
			@Override
			public void run(){

				for(Player player : Bukkit.getOnlinePlayers()){
					GameBadblockPlayer bp = (GameBadblockPlayer) player;
					/*if(bp.isDisguised()){
						bp.getDisguiseEntity().show(p);
					} else */
					if(!bp.isVisible() && bp.getVisiblePredicate().test(p)){
						p.hidePlayer(bp);
					}
					/*if(bp.inGameData(CommandInGameData.class).vanish && !p.hasPermission(GamePermission.BMODERATOR)){
						p.hidePlayer(bp);
					}*/
				}

				if(GameAPI.getAPI().getGameServer().getGameState() == GameState.WAITING){
					if (GameAPI.getAPI().getRunType().equals(RunType.GAME)) {
						List<String> players = new ArrayList<String>();
						double xp = 0;
						double badcoins = 0;

						for (BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers()) {
							PlayerBooster playerBooster = player.getPlayerData().getActiveBooster();

							if (playerBooster != null) {
								xp 		 += playerBooster.getBooster().getXpMultiplier();
								badcoins += playerBooster.getBooster().getCoinsMultiplier();
							}
						}

						if (xp == 0) xp = 1;
						if (badcoins == 0) badcoins = 1;

						if (xp > 1 || badcoins > 1) {
							String o = "[" + StringUtils.join(players, ", ") + "]";

							if (p.getPlayerData().getActiveBooster() != null) {
								for(BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers()){
									player.sendTranslatedMessage("booster.load", Double.toString(xp), Double.toString(badcoins), p.getName(), o);
									player.playSound(Sound.LEVEL_UP);
								}
							} else{
								p.sendTranslatedMessage("booster.resume", Double.toString(xp), Double.toString(badcoins), p.getName(), o);
								p.playSound(Sound.LEVEL_UP);
							}
						}
					}
				}
			}
		}.runTaskLater(GameAPI.getAPI(), 10L);
	}
	
	public static void manageRunningJoin(BadblockPlayer player) {
		GameMessages.joinMessage(GameAPI.getGameName(), player.getName(), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()).broadcast();
		player.setBadblockMode(BadblockMode.PLAYER);
		if (!GameAPI.getAPI().getTeams().isEmpty()) {
			BadblockTeam betterTeam = null;
			for (BadblockTeam team : GameAPI.getAPI().getTeams())
				if (team.playersCurrentlyOnline() < team.getMaxPlayers() && team.playersCurrentlyOnline() > 0) {
					if (betterTeam == null || (betterTeam != null && betterTeam.playersCurrentlyOnline() > team.playersCurrentlyOnline()))
						betterTeam = team;
				}
			if (betterTeam == null) player.kickPlayer("Unable to push you in a team.");
			else betterTeam.joinTeam(player, JoinReason.REBALANCING);
		}
		Bukkit.getPluginManager().callEvent(new PlayerGameInitEvent(player));
		player.setVisible(true);
	}
	
}

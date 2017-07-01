package fr.badblock.game.core18R3.listeners;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.gameserver.threading.GameServerKeeperAliveTask;
import fr.badblock.game.core18R3.listeners.packets.SkullExploitListener;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.players.ingamedata.GameOfflinePlayer;
import fr.badblock.game.core18R3.players.utils.MojangAPI;
import fr.badblock.game.core18R3.players.utils.SkinFactory;
import fr.badblock.game.core18R3.technologies.rabbitlisteners.VanishTeleportListener;
import fr.badblock.gameapi.BadListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.events.PlayerGameInitEvent;
import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent.JoinReason;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.events.api.SpectatorJoinEvent;
import fr.badblock.gameapi.game.GameState;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockPlayer.BadblockMode;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.servers.JoinItems;
import fr.badblock.gameapi.utils.BukkitUtils;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.threading.TaskManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;

/**
 * Listener servant � remplac� la classe CraftPlayer par GameBadblockPlayer et � demander � Ladder les informations joueur.
 * @author LeLanN
 */
public class LoginListener extends BadListener {
	@EventHandler(priority=EventPriority.MONITOR)
	public void onLogin(PlayerLoginEvent e){
		System.out.println("PlayerLoginEvent: " + e.getPlayer().getName());

		if (GameAPI.getAPI().getRunType().equals(RunType.GAME)) {
			if (e.getResult().equals(Result.KICK_FULL) || BukkitUtils.getPlayers().size() >= Bukkit.getMaxPlayers()) {
				if (!VanishTeleportListener.time.containsKey(e.getPlayer().getName().toLowerCase()) || VanishTeleportListener.time.get(e.getPlayer().getName().toLowerCase()) < System.currentTimeMillis()) 
					e.disallow(Result.KICK_FULL, "§cCe serveur est plein.");
				else e.disallow(Result.ALLOWED, "");
			}
		}
		if (!GameAPI.isJoinable()) {
			if (!VanishTeleportListener.time.containsKey(e.getPlayer().getName().toLowerCase()) || VanishTeleportListener.time.get(e.getPlayer().getName().toLowerCase()) < System.currentTimeMillis()) {
				GameAPI.getAPI().getGameServer().keepAlive();
				e.disallow(Result.KICK_FULL, "§cCette partie est en cours.");
			}
		}
		if(GameAPI.getAPI().getWhitelistStatus() && !GameAPI.getAPI().isWhitelisted(e.getPlayer().getName())){
			e.setResult(Result.KICK_WHITELIST); return;
		}

		Reflector 			  reflector 	= new Reflector(ReflectionUtils.getHandle(e.getPlayer()));
		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getName());
		GameBadblockPlayer    player        = null;
		try {
			player = new GameBadblockPlayer((CraftServer) Bukkit.getServer(), (EntityPlayer) reflector.getReflected(), (GameOfflinePlayer) offlinePlayer);
			reflector.setFieldValue("bukkitEntity", player);
		} catch (Exception exception) {
			System.out.println("Impossible de modifier la classe du joueur : ");
			exception.printStackTrace();
		}
		try {
			SkinFactory.applySkin(player, MojangAPI.getSkinPropertyObject(player.getName()));
			//String[] props = MojangAPI.getSkinProperty(e.getPlayer().getName());
			//player.setTextureProperty(props[0], props[1]);
		} catch (Exception exception) {
			System.out.println("Impossible de mettre le skin au joueur : ");
			exception.printStackTrace();
		}
	}

	@EventHandler
	public void onDataReceived(PlayerLoadedEvent e)
	{
		if(GameAPI.getAPI().getRunType() != RunType.DEV || GameServerKeeperAliveTask.isOpenToStaff())
			return;

		if(!e.getPlayer().hasPermission("devserver")) {
			e.getPlayer().kickPlayer("Serveur dév non ouvert au staff !");
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		BadblockPlayer player = (BadblockPlayer) event.getPlayer();
		if (player.getBadblockMode().equals(BadblockMode.SPECTATOR)) {
			if (GameAPI.getAPI().getGameServer().isJoinableWhenRunning()) {
				Inventory inventory = event.getInventory();
				if (inventory != null && inventory.getName() != null && inventory.getSize() < 9 * 2) {
					manageRunningJoin(player);
				}
			}
		}
	}

	@EventHandler
	public void whenLoaded(PlayerLoadedEvent event) {
		GameBadblockPlayer player = (GameBadblockPlayer) event.getPlayer();
		if (player.isGhostConnect() || (VanishTeleportListener.time.containsKey(player.getName().toLowerCase()) && VanishTeleportListener.time.get(player.getName().toLowerCase()) > System.currentTimeMillis())) {
			player.setVisible(false, pl -> !pl.hasPermission("others.mod.ghostconnect"));
			player.setVisible(true, pl -> pl.hasPermission("others.mod.ghostconnect"));
			player.sendTranslatedMessage("game.youjoinedinvanish");
		}
	}

	private HashMap<Player, Integer> lastBookTick = new HashMap<>();

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		GameBadblockPlayer p = (GameBadblockPlayer) e.getPlayer();

		p.loadInjector();
		p.setHasJoined(true);

		Channel channel = ((CraftPlayer)p).getHandle().playerConnection.networkManager.channel;
		if (channel.pipeline().get("bookpacketexploitfix_listener") == null)
		{
			ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler()
			{
				public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
						throws Exception
				{
					super.write(ctx, msg, promise);
				}

				public void channelRead(ChannelHandlerContext ctx, Object msg)
						throws Exception
				{
					if ((msg instanceof PacketPlayInCustomPayload))
					{
						PacketPlayInCustomPayload packet = (PacketPlayInCustomPayload)msg;
						if ((packet.a().equals("MC|BEdit")) || (packet.a().equals("MC|BSign")))
						{
							if ((lastBookTick.containsKey(p)) && (lastBookTick.get(p) + 20 > MinecraftServer.currentTick))
							{
								Bukkit.getScheduler().runTask(GameAPI.getAPI(), new Runnable()
								{
									public void run()
									{
										if (p.isOnline())
										{
											p.kickPlayer("Book edited too quickly!");
											System.out.println("Book edited too quickly by " + p.getName() + "!");
										}
									}
								});
							}
							else
							{
								lastBookTick.put(p, MinecraftServer.currentTick);
								super.channelRead(ctx, msg);
							}
						}
						else {
							super.channelRead(ctx, msg);
						}
					}
					else
					{
						super.channelRead(ctx, msg);
					}
				}
			};
			channel.pipeline().addBefore("packet_handler", "bookpacketexploitfix_listener", channelDuplexHandler);
		}

		BadblockOfflinePlayer offlinePlayer = GameAPI.getAPI().getOfflinePlayer(e.getPlayer().getName());

		if (VanishTeleportListener.time.containsKey(p.getName().toLowerCase()) && VanishTeleportListener.time.get(p.getName().toLowerCase()) > System.currentTimeMillis()) {
			p.setGhostConnect(true);
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
							return;
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
		GameAPI.getAPI().getSqlDatabase().call("SELECT COUNT(*) AS count FROM buyhearts WHERE playerName = '" + p.getName() + "' AND timestmap >= '" + (System.currentTimeMillis() - (86400 * 31 * 1000)) + "'", SQLRequestType.QUERY, new Callback<ResultSet>() {
			@Override
			public void done(ResultSet result, Throwable error) {
				try {
					if (result.next()) {
						int count = result.getInt("count");
						if (count > 0) {
							Bukkit.getScheduler().runTask(GameAPI.getAPI(), new Runnable() {
								@Override
								public void run() {
									if (p.isOnline()) {
										p.setMaxHealth(22);
									}
								}
							});
						}
					}
				}catch(Exception err) {
					error.printStackTrace();
				}
			}
		});

		new BukkitRunnable(){
			@Override
			public void run(){

				for(Player player : Bukkit.getOnlinePlayers()){
					GameBadblockPlayer bp = (GameBadblockPlayer) player;
					/*if(bp.isDisguised()){
						bp.getDisguiseEntity().show(p);
					} else */
					if(bp.getInvisiblePredicate().test(p)){
						p.hidePlayer(bp);
					}
					if(bp.getVisiblePredicate().test(p)){
						p.showPlayer(bp);
					}
					/*if(bp.inGameData(CommandInGameData.class).vanish && !p.hasPermission(GamePermission.BMODERATOR)){
						p.hidePlayer(bp);
					}*/
				}

			}
		}.runTaskLater(GameAPI.getAPI(), 10L);
	}

	public static void manageRunningJoin(BadblockPlayer player) {
		GameMessages.joinMessage(GameAPI.getGameName(), player.getName(), Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()).broadcast();
		player.setBadblockMode(BadblockMode.PLAYER);
		if (!GameAPI.getAPI().getTeams().isEmpty()) {
			BadblockTeam betterTeam = null;
			for (BadblockTeam team : GameAPI.getAPI().getTeams()) {
				if (team.isDead()) continue;
				if (team.playersCurrentlyOnline() < team.getMaxPlayers() && team.playersCurrentlyOnline() > 0) {
					if (betterTeam == null || (betterTeam != null && betterTeam.playersCurrentlyOnline() > team.playersCurrentlyOnline()))
						betterTeam = team;
				}
			}
			if (betterTeam == null) player.kickPlayer("Unable to push you in a team.");
			else betterTeam.joinTeam(player, JoinReason.REBALANCING);
		}
		Bukkit.getPluginManager().callEvent(new PlayerGameInitEvent(player));
		player.setVisible(true);
	}

	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
	public void onInteract(PlayerInteractEvent e)
	{
		if (SkullExploitListener.isExploit(e.getItem()))
		{
			e.setCancelled(true);
			System.out.println("Removing exploit from inventory, " + e.getPlayer().getName());
		}
	}

	@EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=false)
	public void onJoine(PlayerJoinEvent e)
	{
		for (ItemStack item : e.getPlayer().getInventory().getContents()) {
			if (SkullExploitListener.isExploit(item)) {
				System.out.println("Removing exploit from inventory, " + e.getPlayer().getName());
			}
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onItemDrop(PlayerDropItemEvent e)
	{
		if (SkullExploitListener.isExploit(e.getItemDrop().getItemStack()))
		{
			e.setCancelled(true);
			System.out.println("Removing exploit from inventory, " + e.getPlayer().getName());
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void onItemDrop(InventoryClickEvent e)
	{
		if ((e.getWhoClicked().getType() == EntityType.PLAYER) && (SkullExploitListener.isExploit(e.getCurrentItem())))
		{
			e.setCancelled(true);
			System.out.println("Removing exploit from inventory, " + e.getWhoClicked().getName());
		}
	}

	@EventHandler
	public void onLoad(ChunkLoadEvent e)
	{
		SkullExploitListener.cleanChunk(e.getChunk());
	}

	@EventHandler
	public void onLoad2(WorldLoadEvent e)
	{
		for (Chunk c : e.getWorld().getLoadedChunks()) {
			SkullExploitListener.cleanChunk(c);
		}
	}

}

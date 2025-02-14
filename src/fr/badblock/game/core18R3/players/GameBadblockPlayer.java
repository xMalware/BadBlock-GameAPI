package fr.badblock.game.core18R3.players;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.listeners.CustomProjectileListener;
import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.game.core18R3.players.data.GamePlayerData;
import fr.badblock.game.core18R3.players.ingamedata.CommandInGameData;
import fr.badblock.game.core18R3.players.ingamedata.GameOfflinePlayer;
import fr.badblock.game.core18R3.players.utils.BadblockInjector;
import fr.badblock.game.core18R3.players.utils.PlayerLoginWorkers;
import fr.badblock.game.core18R3.players.utils.particle.AuraPlayer;
import fr.badblock.game.core18R3.watchers.MetadataIndex;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.disguise.Disguise;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.fakeentities.FakeEntity.EntityViewList;
import fr.badblock.gameapi.fakeentities.FakeEntity.Visibility;
import fr.badblock.gameapi.game.result.Result;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayBlockAction;
import fr.badblock.gameapi.packets.out.play.PlayCamera;
import fr.badblock.gameapi.packets.out.play.PlayChangeGameState;
import fr.badblock.gameapi.packets.out.play.PlayChangeGameState.GameState;
import fr.badblock.gameapi.packets.out.play.PlayChat;
import fr.badblock.gameapi.packets.out.play.PlayChat.ChatType;
import fr.badblock.gameapi.packets.out.play.PlayEntityEquipment;
import fr.badblock.gameapi.packets.out.play.PlayEntityStatus;
import fr.badblock.gameapi.packets.out.play.PlayEntityStatus.EntityStatus;
import fr.badblock.gameapi.packets.out.play.PlayPlayerInfo;
import fr.badblock.gameapi.packets.out.play.PlayPlayerInfo.PlayerInfo;
import fr.badblock.gameapi.packets.out.play.PlayPlayerInfo.TabAction;
import fr.badblock.gameapi.packets.out.play.PlayPlayerListHeaderFooter;
import fr.badblock.gameapi.packets.out.play.PlayRespawn;
import fr.badblock.gameapi.packets.out.play.PlayTitle;
import fr.badblock.gameapi.packets.out.play.PlayTitle.Action;
import fr.badblock.gameapi.packets.out.play.PlayWorldParticles;
import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;
import fr.badblock.gameapi.packets.watchers.WatcherPig;
import fr.badblock.gameapi.packets.watchers.WatcherWither;
import fr.badblock.gameapi.particles.ParticleEffect;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.RankedPlayer;
import fr.badblock.gameapi.players.bossbars.BossBarColor;
import fr.badblock.gameapi.players.bossbars.BossBarStyle;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.general.StringUtils;
import fr.badblock.gameapi.utils.i18n.I18n;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.selections.CuboidSelection;
import fr.badblock.gameapi.utils.selections.Vector3f;
import fr.badblock.gameapi.utils.threading.TaskManager;
import fr.badblock.permissions.PermissibleGroup;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.permissions.PermissionManager;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerChunkMap;
import net.minecraft.server.v1_8_R3.WorldServer;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.boss.ViaBossBar;

public class GameBadblockPlayer extends CraftPlayer implements BadblockPlayer {
	public static final Type collectionType = new TypeToken<List<String>>() {}.getType();
	public static final Type collectType = new TypeToken<List<Long>>() {}.getType();
	@Getter@Setter
	private CustomObjective 			 customObjective 	  = null;
	@Getter
	private GamePlayerData 				 playerData 		  = null;
	@Getter
	public PermissiblePlayer 			 permissions 		  = null;

	@Getter
	private Map<Class<?>, InGameData> 	 inGameData  		  = null;

	private FakeEntity<WatcherWither> 	 enderdragon 		  = null;

	private List<String>	tempPermissions = new ArrayList<>();
	
	private GameMode 					 gamemodeBefJail 	  = null;
	private FakeEntity<?> 				 fakeJailer 		  = null;

	private BukkitRunnable 				 spectatorRunnable 	  = null;
	@Getter
	private BadblockMode 				 badblockMode 		  = BadblockMode.PLAYER;
	@Setter@Getter
	private boolean 					 dataFetch			  = false;
	@Getter@Setter
	private Environment 				 customEnvironment 	  = null;
	@Getter@Setter
	private BadblockTeam				 team				  = null;
	@Setter
	private boolean						 adminMode			  = false;
	@Getter@Setter
	private boolean						 onlineMode			  = false;
	@Getter@Setter
	JsonObject					 object				  = null;

	@Getter@Setter
	private Vector3f					 firstVector, secondVector;
	private Disguise 	  				disguise;
	@Getter
	private FakeEntity<?> 				disguiseEntity;
	@Getter
	private long 						lastFakeEntityUsedTime;
	@Getter
	private boolean						visible;
	@Getter
	private Predicate<BadblockPlayer> 	visiblePredicate	= p -> true;
	@Getter
	private Predicate<BadblockPlayer> 	invisiblePredicate	= p -> false;
	@Getter@Setter
	private String						realName;
	@Getter@Setter
	private List<UUID>					playersWithHim;
	@Getter@Setter
	public  List<Long>					leaves		   = new ArrayList<>();
	@Getter@Setter
	public boolean 						ghostConnect;
	@Getter@Setter
	private boolean						resultDone;
	@Getter@Setter
	private int							shopPoints;
	@Getter@Setter
	private long						joinTime;
	@Getter@Setter
	private double						moveDist;
	@Getter@Setter
	private long						vlAfk;
	@Getter@Setter
	private String						customRank;
	@Getter@Setter
	private String						customColor;
	@Getter@Setter
	private AuraPlayer					auraPlayer;
	@Getter@Setter
	private boolean						hasJoined;

	@Getter@Setter
	private RankedPlayer				ranked;
	@Getter@Setter
	private int							monthRank = -1;
	@Getter@Setter
	private int							totalRank = -1;
	@Getter@Setter
	private int							totalPoints = -1;

	public GameBadblockPlayer(CraftServer server, EntityPlayer entity, GameOfflinePlayer offlinePlayer) {
		super(server, entity);
		setJoinTime(System.currentTimeMillis());
		this.inGameData  = Maps.newConcurrentMap();

		this.playerData  = offlinePlayer == null ? new GamePlayerData() : offlinePlayer.getPlayerData(); // On initialise pour ne pas provoquer de NullPointerException, mais sera recr�� � la r�c�ptions des donn�es
		this.playerData.setGameBadblockPlayer(this);

		this.permissions = PermissionManager.getInstance().createPlayer(getName(), offlinePlayer == null ? new JsonObject() : offlinePlayer.getObject());

		if(offlinePlayer != null) {
			object = offlinePlayer.getObject();
			team 	   = offlinePlayer.getTeam();
			inGameData = offlinePlayer.getInGameData();
			return;
		}else object = new JsonObject();
		// Load async
		if (!GameAPI.getServerName().startsWith("login"))
		{
			PlayerLoginWorkers.workAsync(this);
		}
	}

	public void loadInjector() {
		try {
			Channel channel = getHandle().playerConnection.networkManager.channel;
			channel.pipeline().addBefore("packet_handler", "api", new BadblockInjector(this));
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
	
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String permission, boolean enable)
	{
		if (tempPermissions == null)
		{
			tempPermissions = new ArrayList<>();
		}
		
		if (enable && !tempPermissions.contains(permission))
		{
			tempPermissions.add(permission);
		}
		else if (!enable && tempPermissions.contains(permission))
		{
			tempPermissions.remove(permission);
		}
		
		return null;
	}

	public void updateData(JsonObject object)
	{
		// Refresh Shop points
		this.refreshShopPoints();

		// Online mode
		if (object.has("onlineMode"))
		{
			this.onlineMode = object.get("onlineMode").getAsBoolean();
		}
		
		// Game
		if (object.has("game"))
		{
			JsonObject game = object.get("game").getAsJsonObject();
			this.object.add("game", game);
			playerData = GameAPI.getGson().fromJson(game, GamePlayerData.class);
			playerData.setData(game);
			playerData.setGameBadblockPlayer(this);
			if (object.has("onlyJoinWhileWaiting"))
			{
				playerData.onlyJoinWhileWaiting = object.get("onlyJoinWhileWaiting").getAsLong();
			}
		}

		// LeaverBuster
		if (object.has("leaves"))
		{
			this.leaves = GameAPI.getGson().fromJson(object.get("leaves").toString(), collectType);
			if (this.leaves == null) this.leaves = new ArrayList<>();
		}
		else
		{
			this.setLeaves(new ArrayList<>());
		}

		// Parties
		if (object.has("playersWithHim"))
		{
			try
			{
				List<String> playersStringWithHim = GameAPI.getGson().fromJson(object.get("playersWithHim").getAsString(), collectionType);
				if (playersWithHim == null)
				{
					playersWithHim = new ArrayList<>();
				}
				else
				{
					playersWithHim.clear();
				}
				playersStringWithHim.forEach(playerString -> playersWithHim.add(UUID.fromString(playerString)));
				if (playersStringWithHim.contains(getUniqueId().toString()))
				{
					playersWithHim.clear();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		if (object.has("permissions"))
		{
			this.object.add("permissions", object.get("permissions"));
			permissions = PermissionManager.getInstance().createPlayer(getRealName() != null ? getRealName() : getName(), object);

			System.out.println("Load Custom Rank.");
			// Custom Rank
			new Thread("loadPlayerCustom-" + getName())
			{
				@Override
				public void run()
				{
					PlayerLoginWorkers.loadCustomRank(GameBadblockPlayer.this);
				}
			}.start();
		}

		// Aura
		if(!isGhostConnect())
		{
			setAuraPlayer(new AuraPlayer(this));
			inGameData(CommandInGameData.class).vanish = true;
		}

		if (getPlayerData().isAura())
		{
			getAuraPlayer().enableAura();
		}

		// Result
		if (getJoinTime() != -1)
		{
			long difference = System.currentTimeMillis() - getJoinTime();
			System.out.println("[API] Loaded player " + getName() + " in " + difference + " ms.");
			setJoinTime(-1);
		}
	}

	@Override
	public void refreshShopPoints()
	{
		String name = getRealName() != null ? getRealName() : getName();
	}

	@Override
	public void addShopPoints(int shopPointsToAdd)
	{
		String name = getRealName() != null ? getRealName() : getName();
	}

	@Override
	public void removeShopPoints(int shopPointsToRemove)
	{
		String name = getRealName() != null ? getRealName() : getName();
	}

	@Override
	public EntityPlayer getHandle() {
		return (EntityPlayer) entity;
	}

	private GameAPI getAPI() {
		return GameAPI.getAPI();
	}

	private I18n getI18n() {
		return getAPI().getI18n();
	}

	@Override
	public boolean isInvulnerable() {
		return getHandle().abilities.isInvulnerable;
	}

	@Override
	public void setInvulnerable(boolean invulnerable) {
		getHandle().abilities.isInvulnerable = invulnerable;
		getHandle().updateAbilities();
	}

	@Override
	public boolean canInstantlyBuild() {
		return getHandle().abilities.canInstantlyBuild;
	}

	@Override
	public void setCanInstantlyBuild(boolean instantlyBuild) {
		getHandle().abilities.canInstantlyBuild = instantlyBuild;
		getHandle().updateAbilities();
	}

	@Override
	public boolean canBuild() {
		return getHandle().abilities.mayBuild;
	}

	@Override
	public void setCanBuild(boolean canBuild) {
		getHandle().abilities.mayBuild = canBuild;
		getHandle().updateAbilities();
	}

	@Override
	public void setReducedDebugInfo(boolean reducedDebugInfo) {
		getAPI().createPacket(PlayEntityStatus.class).setEntityId(getEntityId())
		.setStatus(reducedDebugInfo ? EntityStatus.REDUCED_DEBUG_ENABLE : EntityStatus.REDUCED_DEBUG_DISABLE)
		.send(this);
	}

	@Override
	public void playRain(boolean rain) {
		getAPI().createPacket(PlayChangeGameState.class).setState(GameState.RAINING_START).send(this);
	}

	@Override
	public void showDemoScreen() {
		getAPI().createPacket(PlayChangeGameState.class).setState(GameState.DEMO_MESSAGE).setValue(0).send(this);
	}

	@Override
	public void heal() {
		setFireTicks(0);
		setArrowsInBody((byte) 0);
		removeBadPotionEffects();
		feed();

		setHealth(getMaxHealth());
	}

	@Override
	public void feed() {
		setFoodLevel(20);
		setSaturation(10);
	}

	@Override
	public void clearInventory() {
		getInventory().clear();
		getInventory().setArmorContents(new ItemStack[getInventory().getArmorContents().length]);
	}

	@Override
	public void removePotionEffects() {
		for (PotionEffectType type : PotionEffectType.values()) {
			if (type != null)
				removePotionEffect(type);
		}
	}

	@Override
	public void removeBadPotionEffects() {
		removePotionEffect(PotionEffectType.BLINDNESS);
		removePotionEffect(PotionEffectType.CONFUSION);
		removePotionEffect(PotionEffectType.HARM);
		removePotionEffect(PotionEffectType.HUNGER);
		removePotionEffect(PotionEffectType.POISON);
		removePotionEffect(PotionEffectType.SLOW);
		removePotionEffect(PotionEffectType.SLOW_DIGGING);
		removePotionEffect(PotionEffectType.WEAKNESS);
		removePotionEffect(PotionEffectType.WITHER);
	}

	@Override
	public int getPing() {
		return getHandle().ping;
	}

	@Override
	public void playSound(Sound sound) {
		playSound(getLocation(), sound);
	}

	@Override
	public void playSound(Location location, Sound sound) {
		playSound(location, sound, 3.0f, 1.0f);
	}

	protected Locale getLocale() {
		return playerData.getLocale() == null ? Locale.FRENCH_FRANCE : playerData.getLocale();
	}

	@Override
	public String[] getTranslatedMessage(String key, Object... args) {
		return getI18n().get(getLocale(), key, args);
	}

	@Override
	public void saveOnlineMode()
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("onlineMode", isOnlineMode());
		GameAPI.getAPI().getLadderDatabase().updatePlayerData(this, jsonObject);
	}
	
	@Override
	public void saveGameData() {
		if (isDataFetch()) {
			GameAPI.getAPI().getLadderDatabase().updatePlayerData(this, getPlayerData().saveData());
		}
	}

	@Override
	public void postResult(Result ztoPost) {
		/*long   id	    = new SecureRandom().nextLong();
		long   party    = GamePlugin.getInstance().getGameServer().getGameId();
		String player   = getName().toLowerCase();
		UUID   playerId = getUniqueId();
		String gameType = GameAPI.getGameName();
		String server   = Bukkit.getServerName();*/
		//String result   = GameAPI.getGson().toJson(toPost);


		//	PreparedStatement statement = null;

		try {
			/*statement = GameAPI.getAPI().getSqlDatabase().preparedStatement("INSERT INTO parties(id, party, player, playerId, gametype, servername, day, result)"
					+ " VALUES(?, ?, ?, ?, ?, ?, NOW(), ?)");
			statement.setLong(1, id);
			statement.setLong(2, party);
			statement.setString(3, player);
			statement.setString(4, playerId.toString());
			statement.setString(5, gameType);
			statement.setString(6, server);
			statement.setString(7, result);

			statement.executeUpdate();*/

			if (!resultDone) {
				int percent = (int) Math.round(((double)getPlayerData().getXp() / (double)getPlayerData().getXpUntilNextLevel()) * 100);

				String line = "&a";

				for(int i=0;i<100;i++){
					if(i == percent)
						line += "&8";
					line += "|";
				}
				sendTranslatedMessage("game.result", 
						getPlayerData().getBadcoins(), getPlayerData().getLevel(), percent, getPlayerData().getXp(),
						getPlayerData().getXpUntilNextLevel(), line, "", getPlayerData().getAddedBadcoins(), 
						getPlayerData().getAddedLevels(), getPlayerData().getAddedXP(), getPlayerData().getAddedShopPoints());
				String serverTypeName = Bukkit.getServerName().split("_")[0];
				TextComponent message = new TextComponent( GameAPI.i18n().get("chat.replay")[0] );
				message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/replay") );
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("chat.replay_hover", serverTypeName)[0]).create() ) );
				String autoreplayName = getPlayerData().getReplay() == null || (getPlayerData().getReplay() != null && !getPlayerData().getReplay().contains(serverTypeName)) ? "chat.autoreplay_disable" : "chat.autoreplay_enable";
				String autoreplayHover = getPlayerData().getReplay() == null || (getPlayerData().getReplay() != null && !getPlayerData().getReplay().contains(serverTypeName)) ? "chat.autoreplay_disable_hover" : "chat.autoreplay_enable_hover";
				TextComponent messageAuto = new TextComponent( GameAPI.i18n().get(autoreplayName)[0] );
				messageAuto.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/autoreplay " + serverTypeName) );
				messageAuto.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get(autoreplayHover, serverTypeName)[0]).create() ) );
				TextComponent textComponent = new TextComponent();
				textComponent.setText(" - ");
				TextComponent textComponent2 = new TextComponent();
				textComponent2.setText(" - ");
				TextComponent message2 = new TextComponent( GameAPI.i18n().get("chat.hub")[0] );
				message2.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/hub") );
				message2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("chat.hub_hover")[0]).create() ) );
				this.sendMessage(message, textComponent, messageAuto, textComponent2, message2);
				
				TextComponent voteMap = new TextComponent( GameAPI.i18n().get("votemap.message")[0] );
				
				TextComponent voteMap1 = new TextComponent( GameAPI.i18n().get("votemap.1_name")[0] );
				voteMap1.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/votemap 1") );
				voteMap1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("votemap.1_lore")[0]).create() ) );
				
				TextComponent voteMap2 = new TextComponent( GameAPI.i18n().get("votemap.2_name")[0] );
				voteMap2.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/votemap 2") );
				voteMap2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("votemap.2_lore")[0]).create() ) );

				TextComponent voteMap3 = new TextComponent( GameAPI.i18n().get("votemap.3_name")[0] );
				voteMap3.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/votemap 3") );
				voteMap3.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("votemap.3_lore")[0]).create() ) );

				TextComponent voteMap4 = new TextComponent( GameAPI.i18n().get("votemap.4_name")[0] );
				voteMap4.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/votemap 4") );
				voteMap4.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("votemap.4_lore")[0]).create() ) );

				TextComponent voteMap5 = new TextComponent( GameAPI.i18n().get("votemap.5_name")[0] );
				voteMap3.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/votemap 5") );
				voteMap3.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(GameAPI.i18n().get("votemap.5_lore")[0]).create() ) );
				
				TextComponent voteSeparator = new TextComponent( GameAPI.i18n().get("votemap.separator")[0] );
				voteMap.addExtra(voteMap1);
				voteMap.addExtra(voteSeparator);
				voteMap.addExtra(voteMap2);
				voteMap.addExtra(voteSeparator);
				voteMap.addExtra(voteMap3);
				voteMap.addExtra(voteSeparator);
				voteMap.addExtra(voteMap4);
				voteMap.addExtra(voteSeparator);
				voteMap.addExtra(voteMap5);
				
				sendMessage(voteMap);
				
				resultDone = true;
			}

			saveGameData();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			/*if(statement != null)
				try {
					statement.close();
				} catch (SQLException e){}*/
		}

	}

	@Override
	public void sendTranslatedMessage(String key, Object... args) {
		sendMessage(getTranslatedMessage(key, args));
	}

	@Override
	public void sendActionBar(String message) {
		message = getI18n().replaceColors(message);
		getAPI().createPacket(PlayChat.class).setType(ChatType.ACTION)
		.setContent(message).send(this);
	}

	@Override
	public void sendTranslatedActionBar(String key, Object... args) {
		sendActionBar(getTranslatedMessage(key, args)[0]);
	}

	private Map<String, ViaBossBar> bossBars    = new HashMap<>();
	private ViaBossBar	            lastBossBar = null;

	@Override
	public void addBossBar(String key, String message, float life, BossBarColor color, BossBarStyle style) {
		message = getI18n().replaceColors(message);

		if(bossBars.containsKey(key.toLowerCase())){
			changeBossBar(key, message);
			changeBossBarStyle(key, life, color, style);

			return;
		}

		ViaBossBar bar = new ViaBossBar(message, life, us.myles.ViaVersion.api.boss.BossColor.valueOf(color.name()), us.myles.ViaVersion.api.boss.BossStyle.valueOf(style.name()));
		bar.addPlayer(this);

		bossBars.put(key.toLowerCase(), bar);
		lastBossBar = bar;

		if (enderdragon == null && getProtocolVersion() < BadblockPlayer.VERSION_1_9){
			Location loc = getLocation().clone();
			loc.add(loc.getDirection().multiply(50.0D));

			enderdragon = GameAPI.getAPI().spawnFakeLivingEntity(loc, EntityType.WITHER, WatcherWither.class); // en
			enderdragon.getWatchers().setCustomName(message).setCustomNameVisible(true).setInvisibile(true);
			enderdragon.setVisibility(Visibility.PLAYER);
			enderdragon.addPlayer(EntityViewList.WHITELIST, this);

			new BossBarRunnable().runTaskTimer(getAPI(), 0, 20L);
		}
	}

	@Override
	public void changeBossBar(String key, String message) {
		ViaBossBar bar = bossBars.get(key.toLowerCase());

		if(bar != null){
			message = getI18n().replaceColors(message);
			bar.setTitle(message);

			lastBossBar = bar;
		}
	}

	@Override
	public void changeBossBarStyle(String key, float life, BossBarColor color, BossBarStyle style) {
		ViaBossBar bar = bossBars.get(key.toLowerCase());

		if(bar != null){
			bar.setHealth(life);
			bar.setColor(BossColor.valueOf(color.name()));
			bar.setStyle(BossStyle.valueOf(style.name()));

			lastBossBar = bar;
		}
	}

	@Override
	public void removeBossBar(String key) {
		ViaBossBar bar = bossBars.get(key.toLowerCase());

		if(bar != null){
			bar.removePlayer(this);
			bossBars.remove(key.toLowerCase());


			if(!bossBars.isEmpty()){
				lastBossBar = bossBars.values().iterator().next();
			} else {
				lastBossBar = null;
				enderdragon = null;
			}
		}
	}

	@Override
	public void removeBossBars() {
		bossBars.values().forEach(bar -> bar.removePlayer(this));

		bossBars.clear();

		lastBossBar = null;
		enderdragon = null;
	}

	@Override
	public void sendTranslatedBossBar(String key, Object... args) {
		sendBossBar(getTranslatedMessage(key, args)[0]);
	}

	@Override
	public void sendTitle(String title, String subtitle) {
		title = getI18n().replaceColors(title);
		subtitle = getI18n().replaceColors(subtitle);

		getAPI().createPacket(PlayTitle.class).setAction(Action.RESET).send(this);
		getAPI().createPacket(PlayTitle.class).setAction(Action.TITLE).setContent(title).send(this);
		getAPI().createPacket(PlayTitle.class).setAction(Action.SUBTITLE).setContent(subtitle).send(this);
	}

	@Override
	public void sendTranslatedTitle(String key, Object... args) {
		String[] messages = getI18n().get(getLocale(), key, args);

		String title = messages[0];
		String subtitle = "";

		if (messages.length > 1)
			subtitle = messages[1];
		sendTitle(title, subtitle);
	}

	@Override
	public void clearTitle(){
		getAPI().createPacket(PlayTitle.class).setAction(Action.RESET).send(this);
	}

	@Override
	public void sendTimings(long fadeIn, long stay, long fadeOut) {
		getAPI().createPacket(PlayTitle.class).setAction(Action.TIMES).setFadeIn(fadeIn).setStay(stay)
		.setFadeOut(fadeOut).send(this);
	}

	@Override
	public void sendTabHeader(String header, String footer) {
		header = getI18n().replaceColors(header);
		footer = getI18n().replaceColors(footer);

		getAPI().createPacket(PlayPlayerListHeaderFooter.class).setHeader(header).setFooter(footer).send(this);
	}

	@Override
	public void sendTranslatedTabHeader(TranslatableString header, TranslatableString footer) {
		sendTabHeader(StringUtils.join(header.get(this), "\\n"), StringUtils.join(footer.get(this), "\\n"));
	}

	@Override
	public void showFloatingText(String text, Location location, long lifeTime, double offset) {
		text = getI18n().replaceColors(text);

		if (location == null) {
			location = getEyeLocation().add(getEyeLocation().getDirection().multiply(2.0d));
		}

		location.subtract(0, 1.975, 0); // On enl�ve la hauteur de l'armor stand

		if (offset != 0)
			location.add(MathsUtils.doubleRandomInclusive(-offset, offset),
					MathsUtils.doubleRandomInclusive(-offset, offset),
					MathsUtils.doubleRandomInclusive(-offset, offset));

		FakeEntity<WatcherArmorStand> entity = GameAPI.getAPI().spawnFakeArmorStand(location);

		entity.getWatchers().setInvisibile(true).setCustomName(text).setCustomNameVisible(true).setOnFire(false);

		entity.updateWatchers();
		entity.setVisibility(Visibility.PLAYER);
		entity.addPlayer(EntityViewList.WHITELIST, this);

		if (lifeTime > 0) {
			new BukkitRunnable() {
				@Override
				public void run() {
					entity.remove();
				}
			}.runTaskLater(getAPI(), lifeTime);
		}
	}

	@Override
	public void showTranslatedFloatingText(Location location, long lifeTime, double offset, String key, Object... args) {
		showFloatingText(getTranslatedMessage(key, args)[0], location, lifeTime, offset);
	}

	@Override
	public void jailPlayerAt(Location location) {
		if (location == null) {
			fakeJailer.remove();
			setGameMode(gamemodeBefJail);

			getAPI().createPacket(PlayCamera.class).setEntityId(getEntityId()).send(this);

			fakeJailer = null;
		} else {
			gamemodeBefJail = getGameMode();
			setGameMode(GameMode.SPECTATOR);

			teleport(location);
			fakeJailer = getAPI().spawnFakeLivingEntity(location, EntityType.BAT, WatcherPig.class);
			fakeJailer.getWatchers().setInvisibile(true);
			//fakeJailer.show(this); visibility server ?
			BadblockPlayer player = this;

			new BukkitRunnable() {
				int time = 20;

				@Override
				public void run() {
					time--;

					getAPI().createPacket(PlayCamera.class).setEntityId(fakeJailer.getId()).send(player);

					if(time == 0)
						cancel();
				}
			}.runTaskTimer(GameAPI.getAPI(), 0, 1L);

		}
	}

	@Getter private Location centerJail = null;
	@Getter private double   radius 	= 0.0d;

	@Override
	public void pseudoJail(Location location, double radius) {
		this.centerJail = location;
		this.radius		= radius;
	}

	@Override
	public boolean isJailed() {
		return fakeJailer != null;
	}

	@Override
	public boolean isPseudoJailed(){
		return centerJail != null;
	}

	@Override
	public void playChestAnimation(Block block, boolean open) {
		getAPI().createPacket(PlayBlockAction.class).setBlockPosition(new Vector3f(block.getLocation()))
		.setBlockType(Material.CHEST).setByte1((byte) 1).setByte2((byte) (open ? 1 : 0)).send(this);
	}

	@Override
	public void setEntityCollision(boolean collision) {
		getHandle().collidesWithEntities = collision;
	}

	@Override
	public void setArrowsInBody(byte amount) {
		getHandle().getDataWatcher().watch(MetadataIndex.ARROW_COUNT.getIndex(), amount);
	}

	@Override
	public void changePlayerDimension(Environment world) {
		if(customEnvironment != null && customEnvironment == world) return;
		if(world == getWorld().getEnvironment()) return;

		customEnvironment = world;

		getAPI().createPacket(PlayRespawn.class).setDimension(world).setDifficulty(getWorld().getDifficulty())
		.setGameMode(getGameMode()).setWorldType(getWorld().getWorldType()).send(this);

		setMaxHealth(getMaxHealth());

		for(Player player : Bukkit.getOnlinePlayers()){
			hidePlayer(player);
			showPlayer(player);
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				reloadMap();
				updateInventory();				
			}
		}.runTaskLater(GameAPI.getAPI(), 10L);
	}

	@Override
	public void reloadMap() {
		WorldServer server = (WorldServer) ReflectionUtils.getHandle(getWorld());
		try {
			PlayerChunkMap map = ((PlayerChunkMap) new Reflector(server).getFieldValue("manager"));
			map.removePlayer(getHandle());
			map.addPlayer(getHandle());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateChunk(Chunk chunk) {
		getHandle().chunkCoordIntPairQueue.add(new ChunkCoordIntPair(chunk.getX(), chunk.getZ()));
	}

	@Override
	public void sendPacket(BadblockOutPacket packet) {
		try {
			Packet<?> nmsPacket = ((GameBadblockOutPacket) packet).buildPacket(this);
			getHandle().playerConnection.sendPacket(nmsPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendParticle(Location location, ParticleEffect effect) {
		getAPI().createPacket(PlayWorldParticles.class).setLocation(location).setParticle(effect).send(this);
	}

	@Override
	public void showCustomObjective(CustomObjective objective) {
		if(objective == null) return;

		objective.showObjective(this);
	}

	@Override
	public <T extends InGameData> T inGameData(Class<T> clazz) {
		try {
			if (!inGameData.containsKey(clazz)) {
				inGameData.put(clazz, (InGameData) clazz.getConstructor().newInstance());
			}

			return clazz.cast(inGameData.get(clazz));
		} catch (Exception e) {
			e.printStackTrace();
			GameAPI.logError("Invalid InGameData class (" + clazz + ") ! Return null.");
			return null;
		}
	}

	@Override
	public boolean hasPermission(GamePermission permission) {
		return permission.getPermission() == null ? true : hasPermission(permission.getPermission()) || (tempPermissions != null && tempPermissions.contains(permission.getPermission()));
	}

	@Override
	public boolean hasPermission(String permission) {
		if(GameAPI.getAPI().getRunType() == RunType.DEV && permissions.hasPermission("devserver"))
			return true;
		return permission == null ? true : permissions.hasPermission(permission) || (tempPermissions != null && tempPermissions.contains(permission));
	}

	@Override
	public TranslatableString getGroupPrefix() {
		if (customRank != null && !customRank.isEmpty())
		{
			TranslatableString result = new TranslatableString(null);
			result.setOverrideString(customRank);
			return result;
		}
		return new TranslatableString("permissions.chat." + getFakeMainGroup());
	}

	@Override
	public TranslatableString getGroupSuffix() {
		if (customColor != null && !customColor.isEmpty())
		{
			TranslatableString result = new TranslatableString(null);
			result.setOverrideString(customColor);
			return result;
		}
		return new TranslatableString("permissions.chat_suffix." + getFakeMainGroup());
	}

	@Override
	public TranslatableString getTabGroupPrefix() {
		if (customRank != null && !customRank.isEmpty())
		{
			TranslatableString result = new TranslatableString(null);
			result.setOverrideString(customRank);
			return result;
		}
		return new TranslatableString("permissions.tab." + getFakeMainGroup());
	}

	public String getFakeMainGroup()
	{
		String rankName = permissions.getParent().getName();
		if (getRealName() != null)
		{
			if (rankName.equalsIgnoreCase("gradeperso"))
			{
				return rankName;
			}
			List<String> groups = new ArrayList<>(permissions.getAlternateGroups().keySet());
			groups.add(permissions.getSuperGroup());
			rankName = "default";
			int rankLevel = 0;
			for (String group : groups)
			{
				PermissibleGroup g = PermissionManager.getInstance().getGroup(group);
				if (!g.isStaff())
				{
					if (g.getPower() > rankLevel)
					{
						rankName = g.getName();
						rankLevel = g.getPower();
					}
				}
			}
		}
		return rankName;
	}

	@Override
	public String getMainGroup() {
		return permissions.getSuperGroup();
	}

	@Override
	public Collection<String> getAlternateGroups() {
		return permissions.getAlternateGroups().keySet();
	}

	@Override
	public void sendPlayer(String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(getRealName() != null ? getRealName() : getName());
		out.writeUTF(server);
		sendPluginMessage(GameAPI.getAPI(), "BungeeCord", out.toByteArray());
	}

	@Override
	public void setBadblockMode(BadblockMode newMode) {
		if (newMode != badblockMode) {
			if (newMode == BadblockMode.PLAYER) {
				if (spectatorRunnable != null) {
					spectatorRunnable.cancel();
					spectatorRunnable = null;
				}

				setGameMode(GameMode.SURVIVAL);
			} else if (spectatorRunnable == null) {
				setGameMode(GameMode.SPECTATOR);

				if(!isPseudoJailed() || newMode == BadblockMode.SPECTATOR){
					spectatorRunnable = new TooFarRunnable();
					spectatorRunnable.runTaskTimer(GameAPI.getAPI(), 0, 20L);
				}
			}

			badblockMode = newMode;
		}
	}

	class TooFarRunnable extends BukkitRunnable {
		@Override
		public void run() {
			if (!isOnline()) {
				cancel();
				return;
			}

			Player closestPlayer = null;
			double minDistance = Integer.MAX_VALUE;

			for (BadblockPlayer online : GameAPI.getAPI().getOnlinePlayers()) {
				if (!able(online))
					continue;

				double distance = getLocation().distance(online.getLocation());

				if (distance < minDistance) {
					minDistance = distance;
					closestPlayer = online;
				}
			}

			if (minDistance > 32.0D) {
				Player tp = closestPlayer;
				if (tp == null)
					tp = getRandomNonSpecPlayer();

				if (tp != null){
					teleport(tp);
					GameMessages.doNotGoTooFarWhenSpectator().send(GameBadblockPlayer.this);
				}
			}
		}

		public Player getRandomNonSpecPlayer() {
			for (BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers()) {
				if(able(player))
					return player;
			}

			return null;
		}

		public boolean able(BadblockPlayer player){
			return !player.getUniqueId().equals(getUniqueId()) && player.getGameMode() != GameMode.SPECTATOR && player.getBadblockMode() == BadblockMode.PLAYER
					&& !player.getLocation().getWorld().equals(getLocation().getWorld());
		}
	}

	class BossBarRunnable extends BukkitRunnable {
		private int time = 2;
		private String lastMessage = null;

		@Override
		public void run() {
			if (lastBossBar == null || enderdragon == null || !isOnline()) { // message
				cancel();
				return;
			}

			time--;

			if (time == 0) {
				Location loc = getLocation().clone();

				if(getGameMode() == GameMode.SPECTATOR) {
					loc.add(loc.getDirection().multiply(100.0D));
				} else {
					loc.add(loc.getDirection().multiply(50.0D));
				}

				if (enderdragon.getLocation().distance(loc) > 128.0d) { // trop
					enderdragon.teleport(loc);
					enderdragon.removePlayer(EntityViewList.WHITELIST, GameBadblockPlayer.this);
					enderdragon.addPlayer(EntityViewList.WHITELIST, GameBadblockPlayer.this);
				} else {
					enderdragon.teleport(loc); // on t�l�porte l'entit� pour
				}

				String title = lastBossBar.getTitle();

				if(!title.equals(lastMessage)){
					enderdragon.getWatchers().setCustomName(title);
					enderdragon.updateWatchers();

					lastMessage = title;
				}

				time = 8;
			}
		}
	}

	@Override
	public CuboidSelection getSelection() {
		if(firstVector != null && secondVector != null){
			return new CuboidSelection(getWorld().getName(), firstVector, secondVector);
		}

		return null;
	}

	@Override
	public boolean hasAdminMode(){
		return adminMode;
	}

	@Override
	public void disguise(Disguise disguise) {
		if(isDisguised()){
			disguiseEntity.destroy();
		}

		this.disguise  = disguise;
		disguiseEntity = disguise.createEntity(this);

		getHandle().setInvisible(true);

		if(!disguise.isCanSeeHimself())
			disguiseEntity.addPlayer(EntityViewList.BLACKLIST, this);

		// update equipment for other players :o
		for(Player player : Bukkit.getOnlinePlayers()){
			BadblockPlayer bp = (BadblockPlayer) player;

			if(bp.getUniqueId().equals(getUniqueId()) && !disguise.isCanSeeHimself()){
				continue;
			}

			if(bp.getUniqueId().equals(getUniqueId())) continue;

			for(int i=0;i<5;i++){
				GameAPI.getAPI().createPacket(PlayEntityEquipment.class)
				.setEntityId(getEntityId())
				.setItemStack(null)
				.send(bp);
			}
		}

	}

	@Override
	public boolean isDisguised(){
		return disguise != null && disguiseEntity != null;
	}

	@Override
	public void undisguise() {
		if(isDisguised()){
			this.disguise 		= null;
			this.disguiseEntity.destroy();
			this.disguiseEntity = null;

			getHandle().setInvisible(false);
		}
	}

	@Override
	public int countItems(Material type, byte data) {
		int result = 0;

		for(ItemStack item : getInventory().getContents()){
			if(ItemStackUtils.isValid(item) && item.getType() == type && item.getDurability() == data){
				result += item.getAmount();
			}
		}

		return result;
	}

	@Override
	public int removeItems(Material type, byte data, int amount) {

		for(int i=0;i<getInventory().getContents().length;i++){
			ItemStack item = getInventory().getContents()[i];

			if(ItemStackUtils.isValid(item) && item.getType() == type && item.getDurability() == data){

				if(amount != -1){

					int to = 0;

					if(amount < item.getAmount()){
						to = item.getAmount() - amount;
						amount = 0;
					} else amount -= item.getAmount();

					if(to <= 0)
						getInventory().setItem(i, null);
					else item.setAmount(to);
				} else getInventory().setItem(i, null);

				if(amount == 0)
					break;
			}
		}

		updateInventory();

		return amount;
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<T> projectile, BiConsumer<Block, Entity> action) {
		return launchProjectile(projectile,  action, 0);
	}

	@Override
	public <T extends Projectile> T launchProjectile(Class<T> projectile, BiConsumer<Block, Entity> action, int range) {
		T proj = launchProjectile(projectile);
		proj.setMetadata(CustomProjectileListener.metadataKey, new ProjectileMetadata(action));

		if(range <= 0)
			return proj;

		Vector velocity  = proj.getVelocity();
		Location initLoc = proj.getLocation();

		new BukkitRunnable() {
			@Override
			public void run() {
				if(proj.isDead() || !proj.isValid() || proj.getLocation().distance(initLoc) > range){
					cancel();
					return;
				}

				proj.setVelocity(velocity);
			}
		}.runTaskTimer(GameAPI.getAPI(), 0, 1L);

		return proj;
	}

	@AllArgsConstructor
	public static class ProjectileMetadata implements MetadataValue {
		private BiConsumer<Block, Entity> value;

		@Override
		public BiConsumer<Block, Entity> value() {
			return value;
		}

		@Override
		public void invalidate() {
			value = null;
		}

		@Override
		public Plugin getOwningPlugin() {
			return GameAPI.getAPI();
		}

		@Override
		public String asString() {
			return "";
		}

		@Override
		public short asShort() {
			return 0;
		}

		@Override
		public long asLong() {
			return 0;
		}

		@Override
		public int asInt() {
			return 0;
		}

		@Override
		public float asFloat() {
			return 0;
		}

		@Override
		public double asDouble() {
			return 0;
		}

		@Override
		public byte asByte() {
			return 0;
		}

		@Override
		public boolean asBoolean() {
			return false;
		}
	}

	@Override
	public void useFakeEntity() {
		this.lastFakeEntityUsedTime = System.currentTimeMillis() + 500; // décalage de 500ms pour les double packets
	}

	@Override
	public void setVisible(boolean visible) {
		setVisible(visible, player -> true);
	}

	@Override
	public void setVisible(boolean visible, Predicate<BadblockPlayer> visiblePredicate) {
		this.visible = visible;
		if(visible){
			this.visiblePredicate = visiblePredicate;
			GameAPI.getAPI().getOnlinePlayers().stream().filter(visiblePredicate).forEach(player -> player.showPlayer(this));
		} else {
			this.invisiblePredicate = visiblePredicate;
			GameAPI.getAPI().getOnlinePlayers().stream().filter(visiblePredicate).forEach(player -> player.hidePlayer(this));
		}
	}

	@SuppressWarnings("deprecation")@Override
	public int getProtocolVersion() {
		return us.myles.ViaVersion.api.ViaVersion.getInstance().getPlayerVersion(this);
	}

	@Override
	public <T> T getPermissionValue(String key, Class<T> clazz) {
		JsonElement el = permissions.getValue(key);

		return el == null ? null : GameAPI.getGson().fromJson(permissions.getValue(key), clazz);
	}

	@Override
	public int getVipLevel() {
		Integer res = getPermissionValue("badblock.viplevel", Integer.class);
		return res == null ? 0 : (int) res;
	}

	@Override
	public boolean hasVipLevel(int level, boolean showErrorMessage) {
		boolean have = getVipLevel() >= level;

		if(!have && showErrorMessage){
			sendTranslatedMessage("game.vip.needlevel." + level);
		}

		return have;
	}

	@Override
	public boolean canOnlyJoinWhileWaiting() {
		return this.getPlayerData().onlyJoinWhileWaiting > System.currentTimeMillis();
	}

	@Override
	public void setOnlyJoinWhileWaiting(long time) {
		this.getPlayerData().onlyJoinWhileWaiting = time;
	}

	@Override
	public void setPlayerSkin(String skinUrl) {
		setPlayerSkin0(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinUrl));
	}

	@Override
	public void setPlayerSkin(String skinUrl, String capeUrl) {
		setPlayerSkin0(String.format("{textures:{SKIN:{url:\"%s\"},CAPE:{url\"%s\"}}", skinUrl, capeUrl));
	}

	private void setPlayerSkin0(String textures)
	{
		PropertyMap map = this.getHandle().getProfile().getProperties();

		map.clear();

		byte[] encodedData = Base64.getEncoder().encode(textures.getBytes());
		map.put("textures", new Property("textures", new String(encodedData)));

		updatePlayerSkin();
	}

	@Override
	public void setTextureProperty(String value, String signature) {
		TaskManager.runTask(new Runnable() {
			@Override
			public void run() {
				PropertyMap map = getHandle().getProfile().getProperties();
				map.clear();
				map.put("textures", new Property("textures", value, signature));
				updatePlayerSkin();
			}
		});
	}

	public void updatePlayerSkin()
	{
		resetPlayerView();

		for(BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers())
		{
			if(player == this || !player.canSee(this))
				return;

			player.hidePlayer(this);
			player.showPlayer(this);
		}
	}

	public void resetPlayerView()
	{
		Environment env = customEnvironment == null ? getWorld().getEnvironment() : customEnvironment;


		getAPI().createPacket(PlayPlayerInfo.class).setAction(TabAction.REMOVE_PLAYER).addPlayer(getUniqueId(), createPlayerInfo()).send(this);
		getAPI().createPacket(PlayPlayerInfo.class).setAction(TabAction.ADD_PLAYER).addPlayer(getUniqueId(), createPlayerInfo()).send(this);

		getAPI().createPacket(PlayRespawn.class).setDimension(env).setDifficulty(getWorld().getDifficulty()).setGameMode(getGameMode()).setWorldType(getWorld().getWorldType()).send(this);

		updateInventory();

		updateScaledHealth();
		getHandle().triggerHealthUpdate();

		getInventory().setHeldItemSlot(getInventory().getHeldItemSlot());
		setItemInHand(getItemInHand());
		getHandle().updateAbilities();
	}

	private PlayerInfo createPlayerInfo()
	{
		return new PlayerInfo(getUniqueId(), getName(), getHandle().getProfile().getProperties(), getGameMode(), getPing(), getDisplayName());
	}

}
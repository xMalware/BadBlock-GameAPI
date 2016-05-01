package fr.badblock.game.v1_8_R3;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;

import fr.badblock.game.v1_8_R3.achievements.GameAchievements;
import fr.badblock.game.v1_8_R3.commands.FlyCommand;
import fr.badblock.game.v1_8_R3.commands.GameModeCommand;
import fr.badblock.game.v1_8_R3.commands.LagCommand;
import fr.badblock.game.v1_8_R3.entities.CustomCreatures;
import fr.badblock.game.v1_8_R3.fakeentities.FakeEntities;
import fr.badblock.game.v1_8_R3.game.GameServer;
import fr.badblock.game.v1_8_R3.game.GameServerManager;
import fr.badblock.game.v1_8_R3.i18n.GameI18n;
import fr.badblock.game.v1_8_R3.itemstack.GameCustomInventory;
import fr.badblock.game.v1_8_R3.itemstack.GameItemExtra;
import fr.badblock.game.v1_8_R3.itemstack.GameItemStackFactory;
import fr.badblock.game.v1_8_R3.itemstack.ItemStackExtras;
import fr.badblock.game.v1_8_R3.jsonconfiguration.APIConfig;
import fr.badblock.game.v1_8_R3.jsonconfiguration.DefaultKitContentManager;
import fr.badblock.game.v1_8_R3.jsonconfiguration.GameJsonConfiguration;
import fr.badblock.game.v1_8_R3.ladder.GameLadderSpeaker;
import fr.badblock.game.v1_8_R3.listeners.ChangeWorldEvent;
import fr.badblock.game.v1_8_R3.listeners.FakeDeathCaller;
import fr.badblock.game.v1_8_R3.listeners.GameServerListener;
import fr.badblock.game.v1_8_R3.listeners.JailedPlayerListener;
import fr.badblock.game.v1_8_R3.listeners.LoginListener;
import fr.badblock.game.v1_8_R3.listeners.ProjectileHitBlockCaller;
import fr.badblock.game.v1_8_R3.listeners.fixs.ArrowBugFixListener;
import fr.badblock.game.v1_8_R3.listeners.fixs.PlayerTeleportFix;
import fr.badblock.game.v1_8_R3.listeners.fixs.UselessDamageFixListener;
import fr.badblock.game.v1_8_R3.listeners.mapprotector.BlockMapProtectorListener;
import fr.badblock.game.v1_8_R3.listeners.mapprotector.DefaultMapProtector;
import fr.badblock.game.v1_8_R3.listeners.mapprotector.EntityMapProtectorListener;
import fr.badblock.game.v1_8_R3.listeners.mapprotector.PlayerMapProtectorListener;
import fr.badblock.game.v1_8_R3.listeners.packets.CameraListener;
import fr.badblock.game.v1_8_R3.merchant.GameMerchantInventory;
import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket.GameBadblockOutPackets;
import fr.badblock.game.v1_8_R3.packets.out.GameParticleEffect;
import fr.badblock.game.v1_8_R3.players.GameCustomObjective;
import fr.badblock.game.v1_8_R3.players.GameJoinItems;
import fr.badblock.game.v1_8_R3.players.GameKit;
import fr.badblock.game.v1_8_R3.players.GameScoreboard;
import fr.badblock.game.v1_8_R3.players.GameTeam;
import fr.badblock.game.v1_8_R3.watchers.GameWatchers;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.LadderSpeaker;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.InPacketListener;
import fr.badblock.gameapi.packets.OutPacketListener;
import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.particles.ParticleEffect;
import fr.badblock.gameapi.particles.ParticleEffectType;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.PlayerAchievement;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.players.kits.PlayerKitContentManager;
import fr.badblock.gameapi.servers.JoinItems;
import fr.badblock.gameapi.servers.MapProtector;
import fr.badblock.gameapi.utils.CustomObjective;
import fr.badblock.gameapi.utils.JsonConfiguration;
import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.itemstack.DefaultItems;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;
import fr.badblock.gameapi.utils.itemstack.ItemStackFactory;
import fr.badblock.gameapi.utils.merchants.CustomMerchantInventory;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.ladder.api.utils.MathsUtils;
import fr.badblock.permissions.PermissionManager;
import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class GamePlugin extends GameAPI {
	public static final String FOLDER_I18N 		   = "i18n",
							   FOLDER_ACHIEVEMENTS = "achievements",
							   FOLDER_KITS		   = "kits",
							   CONFIG_DATABASES	   = "databases.json";

	@Getter private static GamePlugin   instance;

	@Getter 
	private GameI18n 				    i18n;
	private Map<String, BadblockTeam>   teams				= Maps.newConcurrentMap();
	
	private GameAchievements			achievements;

	@Getter
	private GameServer					gameServer;

	@Getter@Setter@NonNull
	private MapProtector				mapProtector		= new DefaultMapProtector();
	@Getter@Setter@NonNull
	private PlayerKitContentManager     kitContentManager	= new DefaultKitContentManager();
	@Getter
	private JoinItems					joinItems;
	
	
	@Getter
	private GameScoreboard				badblockScoreboard;
	@Getter
	private LadderSpeaker				ladderDatabase;
	@Getter
	private GameServerManager			gameServerManager;

	// Packet system
	@Getter
	private ConcurrentMap<Class<? extends BadblockInPacket>, ConcurrentSet<InPacketListener<?>>>		packetInListeners	= new ConcurrentHashMap<>();
	@Getter
	private ConcurrentMap<Class<? extends BadblockOutPacket>, ConcurrentSet<OutPacketListener<?>>>		packetOutListeners	= new ConcurrentHashMap<>();
	
	@Override
	public void onEnable() {
		instance 	= this;
		GameAPI.API = this;

		i18n  		= new GameI18n(); // Création de l'I18N pour permettre la couleur
		
		try {
			/**
			 * Chargement de la configuration
			 */
			
			GameAPI.logColor("&b[GameAPI] &aLoading API...");
			long nano = System.nanoTime();
			
			i18n.load(new File(getDataFolder(), FOLDER_I18N));
			teams 		 	 = Maps.newConcurrentMap();
			
			GameAPI.logColor("&b[GameAPI] &aLoading achievements...");
			achievements 	 = new GameAchievements(new File(getDataFolder(), FOLDER_ACHIEVEMENTS));
			
			GameAPI.logColor("&b[GameAPI] &aLoading databases configuration...");
			File configFile  = new File(getDataFolder(), CONFIG_DATABASES);

			if(!configFile.exists())
				configFile.createNewFile();
			APIConfig config = JsonUtils.load(configFile, APIConfig.class);

			if(config == null) {
				config = new APIConfig();
				JsonUtils.save(configFile, config, true);
			}

			GameAPI.logColor("&b[GameAPI] &a=> Ladder : " + config.ladderIp + ":" +  config.ladderPort);
			GameAPI.logColor("&b[GameAPI] &aConnecting to Ladder...");
			
			new PermissionManager(new JsonArray());
			ladderDatabase   = new GameLadderSpeaker(config.ladderIp, config.ladderPort);
			ladderDatabase.askForPermissions();


			GameAPI.logColor("&b[GameAPI] &aLoading NMS classes...");
			/**
			 * Chargement des classes NMS
			 */
			CustomCreatures.registerEntities();
			
			GameAPI.logColor("&b[GameAPI] &aRegistering listeners...");
			/**
			 * Chargement des Listeners
			 */
			new LoginListener(); 				// Met le BadblockPlayer à la connection
			new JailedPlayerListener(); 		// Permet de bien jail les joueurs
			new ItemStackExtras(); 		 	    // Permet de bien gérer les items spéciaux
			new ProjectileHitBlockCaller();		// Permet d'appeler un event lorsque un projectile rencontre un block
			new GameServerListener();			// Permet la gestion des joueurs vers Docker
			new FakeDeathCaller();				// Permet de gérer les fausses morts et la détections du joueur
			new ChangeWorldEvent();				// Permet de mieux gérer les fausses dimensions
			joinItems = new GameJoinItems();    // Items donné à l'arrivée du joueur
			
			/** Correction de bugs Bukkit */

			new ArrowBugFixListener();			// Permet de corriger un bug avec les flèches se lançant mal
			new UselessDamageFixListener();		// Enlève les dégats inutiles lors d'une chute
			new PlayerTeleportFix();			// Permet de bien voir le joueur lorsqu'il respawn (bug Bukkit)

			/** Protection et optimisations par défaut */

			new PlayerMapProtectorListener();	// Permet de protéger la map
			new BlockMapProtectorListener();	// Permet de protéger la map
			new EntityMapProtectorListener();	// Permet de protéger la map

			/** Packets écoutés par l'API */
			GameAPI.logColor("&b[GameAPI] &aRegistering packets listeners ...");
			
			new CameraListener().register();	// Packet pour voir à la place du joueur en spec (aucun event sur Bukkit)
			
			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			
			GameAPI.logColor("&b[GameAPI] &aCreating scoreboard...");
			
			badblockScoreboard = new GameScoreboard(); // Permet de gérer le scoreboard

			/**
			 * Chargement des commandes par défaut
			 */
			GameAPI.logColor("&b[GameAPI] &aRegistering commands...");
			new FlyCommand();
			new GameModeCommand();
			new LagCommand();

			GameAPI.logColor("&b[GameAPI] &aGameServer loading...");
			// GameServer après tout
			this.gameServer 	   = new GameServer();
			this.gameServerManager = new GameServerManager(config);
			
			nano = System.nanoTime() - nano;
			
			double ms = (double) nano / 1_000_000d;
			ms = MathsUtils.round(ms, 3);
			
			GameAPI.logColor("&b[GameAPI] &aAPI loaded! (" + ms + "ms)");
		} catch (Throwable t){
			t.printStackTrace();
			
			GameAPI.logColor("&c[GameAPI] Error occurred while loading API. See the stack trace. Restarting...");
			Bukkit.shutdown();
		}
	}

	@Override
	public void onDisable(){
		if(i18n != null)
			i18n.save();
		if(getGameServerManager() != null)
			this.getGameServerManager().stop();
		
		CustomCreatures.unregisterEntities();
	}

	@Override
	public ItemStackFactory createItemStackFactory() {
		return new GameItemStackFactory();
	}

	@Override
	public ItemStackFactory createItemStackFactory(ItemStack item) {
		return new GameItemStackFactory(item);
	}

	@Override
	public ItemStackExtra createItemStackExtra(ItemStack itemStack) {
		return new GameItemExtra(itemStack);
	}

	@Override
	public CustomInventory createCustomInventory(int lines, String displayName) {
		return new GameCustomInventory(displayName, lines);
	}

	@Override
	public Map<String, PlayerKit> loadKits(String game) {
		File kitFolder = new File(new File(getDataFolder(), FOLDER_KITS), game.toLowerCase());
		
		Map<String, PlayerKit> kits = Maps.newConcurrentMap();
		
		if(!kitFolder.exists()) kitFolder.mkdirs();
		if(!kitFolder.isDirectory()) return kits;
		
		for(File file : kitFolder.listFiles()){
			PlayerKit kit = JsonUtils.load(file, GameKit.class);
			
			kits.put(kit.getKitName().toLowerCase(), kit);
		}
		
		return kits;
	}

	@Override
	public void registerTeams(int maxPlayers, ConfigurationSection configuration) {
		teams.clear();
		
		for(String key : configuration.getKeys(false)){
			key = key.toLowerCase();
			BadblockTeam team = new GameTeam(configuration.getConfigurationSection(key), maxPlayers);
			
			teams.put(key, team);
		}
	}

	@Override
	public Collection<BadblockTeam> getTeams() {
		return Collections.unmodifiableCollection(teams.values());
	}

	@Override
	public Collection<String> getTeamsKey() {
		return Collections.unmodifiableCollection(teams.keySet());
	}

	@Override
	public BadblockTeam getTeam(String key) {
		return teams.get(key.toLowerCase());
	}

	@Override
	public void unregisterTeam(BadblockTeam team) {
		teams.values().remove(team);
	}

	@Override
	public CustomMerchantInventory getCustomMarchantInventory() {
		return new GameMerchantInventory();
	}

	@Override
	public Collection<String> getAchievementsGames() {
		return achievements.getGames();
	}

	@Override
	public Collection<PlayerAchievement> getAchievements(String game) {
		return achievements.getAchievements(game);
	}

	@Override
	public PlayerAchievement getAchievement(String key) {
		return achievements.getAchievement(key);
	}

	@Override
	public CustomObjective buildCustomObjective(String name) {
		return new GameCustomObjective(name);
	}

	@SuppressWarnings("unchecked") @Override
	public <T extends BadblockOutPacket> T createPacket(Class<T> clazz) {
		Class<? extends GameBadblockOutPacket> gameClass = GameBadblockOutPackets.getPacketClass(clazz);

		if(gameClass == null) return null;

		try {
			return (T) ReflectionUtils.getConstructor(gameClass).newInstance();
		} catch (Exception e) {
			logError("The packet " + gameClass + " don't have a no-arg-constructor ! Can not use it");
		}
		return null;
	}

	@Override
	public <T extends BadblockInPacket> void listenAtPacket(InPacketListener<T> listener) {
		Class<? extends BadblockInPacket> packet = listener.getGenericPacketClass();
		
		ConcurrentSet<InPacketListener<?>> list = packetInListeners.get(packet);
		if(list == null) {
			list = new ConcurrentSet<>();
			packetInListeners.put(packet, list);
		}

		list.add(listener);
	}

	@Override
	public <T extends BadblockOutPacket> void listenAtPacket(OutPacketListener<T> listener) {
		Class<? extends BadblockOutPacket> packet = listener.getGenericPacketClass();
		
		ConcurrentSet<OutPacketListener<?>> list = packetOutListeners.get(packet);
		if(list == null) {
			list = new ConcurrentSet<>();
			packetOutListeners.put(packet, list);
		}

		list.add(listener);
	}

	@Override
	public <T extends WatcherEntity> T createWatcher(Class<T> clazz) {
		return GameWatchers.buildWatch(clazz);
	}

	@Override
	public <T extends WatcherEntity> FakeEntity<T> spawnFakeLivingEntity(Location location, EntityType type, Class<T> clazz) {
		return FakeEntities.spawnFakeLivingEntity(location, type, clazz);
	}

	@Override
	public FakeEntity<WatcherArmorStand> spawnFakeArmorStand(Location location) {
		return FakeEntities.spawnFakeArmorStand(location);
	}

	@Override
	public ParticleEffect createParticleEffect(ParticleEffectType type) {
		return new GameParticleEffect(type);
	}

	@Override
	public JsonConfiguration getJsonConfiguration() {
		return new GameJsonConfiguration();
	}

	@Override
	public DefaultItems getDefaultItems() {
		return null; //TODO
	}
}

package fr.badblock.game.v1_8_R3;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.badblock.game.v1_8_R3.commands.AdminModeCommand;
import fr.badblock.game.v1_8_R3.commands.FeedCommand;
import fr.badblock.game.v1_8_R3.commands.FlyCommand;
import fr.badblock.game.v1_8_R3.commands.GameModeCommand;
import fr.badblock.game.v1_8_R3.commands.HealCommand;
import fr.badblock.game.v1_8_R3.commands.I18RCommand;
import fr.badblock.game.v1_8_R3.commands.KitsCommand;
import fr.badblock.game.v1_8_R3.commands.LagCommand;
import fr.badblock.game.v1_8_R3.configuration.GameConfiguration;
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
import fr.badblock.game.v1_8_R3.ladder.GameLadderSpeaker;
import fr.badblock.game.v1_8_R3.listeners.ChangeWorldEvent;
import fr.badblock.game.v1_8_R3.listeners.ChatListener;
import fr.badblock.game.v1_8_R3.listeners.DisconnectListener;
import fr.badblock.game.v1_8_R3.listeners.FakeDeathCaller;
import fr.badblock.game.v1_8_R3.listeners.GameServerListener;
import fr.badblock.game.v1_8_R3.listeners.JailedPlayerListener;
import fr.badblock.game.v1_8_R3.listeners.LoginListener;
import fr.badblock.game.v1_8_R3.listeners.MoveListener;
import fr.badblock.game.v1_8_R3.listeners.PlayerSelectionListener;
import fr.badblock.game.v1_8_R3.listeners.ProjectileHitBlockCaller;
import fr.badblock.game.v1_8_R3.listeners.fixs.ArrowBugFixListener;
import fr.badblock.game.v1_8_R3.listeners.fixs.UselessDamageFixListener;
import fr.badblock.game.v1_8_R3.listeners.mapprotector.BlockMapProtectorListener;
import fr.badblock.game.v1_8_R3.listeners.mapprotector.DefaultMapProtector;
import fr.badblock.game.v1_8_R3.listeners.mapprotector.EntityMapProtectorListener;
import fr.badblock.game.v1_8_R3.listeners.mapprotector.PlayerMapProtectorListener;
import fr.badblock.game.v1_8_R3.listeners.packets.CameraListener;
import fr.badblock.game.v1_8_R3.listeners.packets.InteractEntityListener;
import fr.badblock.game.v1_8_R3.merchant.GameMerchantInventory;
import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket.GameBadblockOutPackets;
import fr.badblock.game.v1_8_R3.packets.out.GameParticleEffect;
import fr.badblock.game.v1_8_R3.players.GameCustomObjective;
import fr.badblock.game.v1_8_R3.players.GameJoinItems;
import fr.badblock.game.v1_8_R3.players.GameKit;
import fr.badblock.game.v1_8_R3.players.GameScoreboard;
import fr.badblock.game.v1_8_R3.players.GameTeam;
import fr.badblock.game.v1_8_R3.sql.GameSQLDatabase;
import fr.badblock.game.v1_8_R3.technologies.RabbitSpeaker;
import fr.badblock.game.v1_8_R3.watchers.GameWatchers;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.configuration.BadConfiguration;
import fr.badblock.gameapi.databases.LadderSpeaker;
import fr.badblock.gameapi.events.api.PlayerJoinTeamEvent.JoinReason;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.BadblockInPacket;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.InPacketListener;
import fr.badblock.gameapi.packets.OutPacketListener;
import fr.badblock.gameapi.packets.watchers.WatcherArmorStand;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.particles.ParticleEffect;
import fr.badblock.gameapi.particles.ParticleEffectType;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.players.kits.PlayerKitContentManager;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.badblock.gameapi.servers.JoinItems;
import fr.badblock.gameapi.servers.MapProtector;
import fr.badblock.gameapi.utils.entities.CustomCreature;
import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
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
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.World;

public class GamePlugin extends GameAPI {
	public static final String FOLDER_I18N 		   = "i18n",
							   FOLDER_ACHIEVEMENTS = "achievements",
							   FOLDER_KITS		   = "kits",
							   CONFIG_DATABASES	   = "databases.json";
	public static Thread thread;
	
	@Getter private static GamePlugin   instance;

	@Getter 
	private GameI18n 				    i18n;
	private Map<String, BadblockTeam>   teams				= Maps.newConcurrentMap();
	
	@Getter
	private GameServer					gameServer;

	@Getter@Setter@NonNull
	private MapProtector				mapProtector		= new DefaultMapProtector();
	@Getter
	private boolean						antiSpawnKill		= false;
	@Getter@Setter@NonNull
	private PlayerKitContentManager     kitContentManager	= new DefaultKitContentManager(true);
	@Getter
	private JoinItems					joinItems;
	
	
	@Getter
	private GameScoreboard				badblockScoreboard;
	@Getter
	private GameSQLDatabase				sqlDatabase;
	@Getter
	private LadderSpeaker				ladderDatabase;
	@Getter
	private GameServerManager			gameServerManager;
	@Getter
	private RabbitSpeaker				rabbitSpeaker;
	
	// Packet system
	@Getter
	private ConcurrentMap<Class<? extends BadblockInPacket>, ConcurrentSet<InPacketListener<?>>>		packetInListeners	= new ConcurrentHashMap<>();
	@Getter
	private ConcurrentMap<Class<? extends BadblockOutPacket>, ConcurrentSet<OutPacketListener<?>>>		packetOutListeners	= new ConcurrentHashMap<>();
	
	@Override
	public void onEnable() {
		thread		= Thread.currentThread();
		instance 	= this;
		GameAPI.API = this;

		i18n  		= new GameI18n(); // Création de l'I18N pour permettre la couleur
		
		try {
			/**
			 * Chargement de la configuration
			 */
			
			GameAPI.logColor("&b[GameAPI] &aLoading API...");
			long nano = System.nanoTime();
			
			loadI18n();
			teams 		 	 = Maps.newConcurrentMap();
			
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
			
			GameAPI.logColor("&b[GameAPI] &a=> SQL : " + config.sqlIp + ":" +  config.sqlPort);
			GameAPI.logColor("&b[GameAPI] &aConnecting to SQL...");
			
			sqlDatabase   = new GameSQLDatabase(config.sqlIp, Integer.toString(config.sqlPort), config.sqlUser, config.sqlPassword, config.sqlDatabase);
			sqlDatabase.openConnection();
			
			rabbitSpeaker = new RabbitSpeaker(config);


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
			new DisconnectListener(); 			// Gère la déconnection
			new JailedPlayerListener(); 		// Permet de bien jail les joueurs
			new ItemStackExtras(); 		 	    // Permet de bien gérer les items spéciaux
			new ProjectileHitBlockCaller();		// Permet d'appeler un event lorsque un projectile rencontre un block
			new GameServerListener();			// Permet la gestion des joueurs vers Docker
			new FakeDeathCaller();				// Permet de gérer les fausses morts et la détections du joueur
			new ChangeWorldEvent();				// Permet de mieux gérer les fausses dimensions
			new PlayerSelectionListener();	    // Permet aux administrateurs de définir une zone
			new MoveListener();					// Permet d'empêcher les joueurs de sortir d'une zone
			new ChatListener();					// Permet de formatter le chat
			joinItems = new GameJoinItems();    // Items donné à l'arrivée du joueur
			
			/** Correction de bugs Bukkit */

			new ArrowBugFixListener();			// Permet de corriger un bug avec les flèches se lançant mal
			new UselessDamageFixListener();		// Enlève les dégats inutiles lors d'une chute
			//new PlayerTeleportFix();			// Permet de bien voir le joueur lorsqu'il respawn (bug Bukkit)

			/** Protection et optimisations par défaut */

			new PlayerMapProtectorListener();	// Permet de protéger la map
			new BlockMapProtectorListener();	// Permet de protéger la map
			new EntityMapProtectorListener();	// Permet de protéger la map

			/** Packets écoutés par l'API */
			GameAPI.logColor("&b[GameAPI] &aRegistering packets listeners ...");
			
			new CameraListener().register();	// Packet pour voir à la place du joueur en spec (aucun event sur Bukkit)
			new InteractEntityListener().register();

			getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
			
			GameAPI.logColor("&b[GameAPI] &aCreating scoreboard...");
			
			badblockScoreboard = new GameScoreboard(); // Permet de gérer le scoreboard

			/**
			 * Chargement des commandes par défaut
			 */
			GameAPI.logColor("&b[GameAPI] &aRegistering commands...");
			new AdminModeCommand();
			new FlyCommand();
			new HealCommand();
			new FeedCommand();
			new GameModeCommand();
			new LagCommand();
			new KitsCommand();
			new I18RCommand();

			GameAPI.logColor("&b[GameAPI] &aGameServer loading...");
			// GameServer après tout
			this.gameServer 	   = new GameServer();
			this.gameServerManager = new GameServerManager(config);
			this.getGameServerManager().start();
			
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
		try {
			sqlDatabase.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(i18n != null)
			i18n.save();
		if(getGameServerManager() != null)
			this.getGameServerManager().stop();
		
		CustomCreatures.unregisterEntities();
	}

	public void loadI18n(){
		i18n.load(new File(getDataFolder(), FOLDER_I18N));
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
	public BadblockTeam getTeam(@NonNull String key) {
		return teams.get(key.toLowerCase());
	}

	@Override
	public void unregisterTeam(@NonNull BadblockTeam team) {
		teams.values().remove(team);
	}
	
	@Override
	public void balanceTeams(boolean sameSize) {
		List<BadblockTeam> unfilled = new ArrayList<>();
		
		for(BadblockTeam team : getTeams()){
			if(team.getOnlinePlayers().size() < team.getMaxPlayers()){
				unfilled.add(team);
			}
		}
		
		for(Player player : Bukkit.getOnlinePlayers()){
			BadblockPlayer p = (BadblockPlayer) player;
		
			if(p.getTeam() == null){
				if(unfilled.isEmpty()){
					p.sendPlayer("lobby"); // kick
					continue;
				}
				
				BadblockTeam team = unfilled.get(0);
				
				team.joinTeam(p, JoinReason.REBALANCING);

				if(team.getOnlinePlayers().size() == team.getMaxPlayers()){
					unfilled.remove(0);
				}
			}
		}
		
		if(!unfilled.isEmpty() && sameSize){
			int playersByTeam = Bukkit.getOnlinePlayers().size() / getTeams().size();
			
			for(BadblockTeam team : getTeams()){
				
				if(team.getOnlinePlayers().size() < playersByTeam){
					
					for(BadblockTeam joinable : getTeams()){
						GameTeam game = (GameTeam) joinable;
						
						if(joinable.getOnlinePlayers().size() > playersByTeam){
							team.joinTeam(game.getRandomPlayer(), JoinReason.REBALANCING);
						}
					
						if(team.getOnlinePlayers().size() == playersByTeam)
							break;
					}
					
				}
				
			}
		}
	}
	
	@Override
	public void formatChat(boolean enabled, boolean team){
		formatChat(enabled, team, null);
	}
	
	@Override
	public void formatChat(boolean enabled, boolean team, String custom){
		ChatListener.enabled = enabled;
		ChatListener.team    = team;
		ChatListener.custom  = custom;
	}

	@Override
	public BadblockOfflinePlayer getOfflinePlayer(@NonNull UUID uniqueId) {
		return gameServer.getPlayers().get(uniqueId);
	}
	
	@Override
	public CustomMerchantInventory getCustomMerchantInventory() {
		return new GameMerchantInventory();
	}

	@Override
	public CustomObjective buildCustomObjective(@NonNull String name) {
		return new GameCustomObjective(name);
	}

	@SuppressWarnings("unchecked") @Override
	public <T extends BadblockOutPacket> T createPacket(@NonNull Class<T> clazz) {
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
	public <T extends BadblockInPacket> void listenAtPacket(@NonNull InPacketListener<T> listener) {
		Class<? extends BadblockInPacket> packet = listener.getGenericPacketClass();
		
		ConcurrentSet<InPacketListener<?>> list = packetInListeners.get(packet);
		if(list == null) {
			list = new ConcurrentSet<>();
			packetInListeners.put(packet, list);
		}

		list.add(listener);
	}

	@Override
	public <T extends BadblockOutPacket> void listenAtPacket(@NonNull OutPacketListener<T> listener) {
		Class<? extends BadblockOutPacket> packet = listener.getGenericPacketClass();
		
		ConcurrentSet<OutPacketListener<?>> list = packetOutListeners.get(packet);
		if(list == null) {
			list = new ConcurrentSet<>();
			packetOutListeners.put(packet, list);
		}

		list.add(listener);
	}

	@Override
	public void enableAntiSpawnKill() {
		antiSpawnKill = true;
	}
	
	@Override
	public <T extends WatcherEntity> T createWatcher(@NonNull Class<T> clazz) {
		return GameWatchers.buildWatch(clazz);
	}

	@Override
	public <T extends WatcherEntity> FakeEntity<T> spawnFakeLivingEntity(@NonNull Location location, @NonNull EntityType type, @NonNull Class<T> clazz) {
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
	public BadConfiguration loadConfiguration(File file) {
		return loadConfiguration(JsonUtils.loadObject(file));
	}
	
	@Override
	public BadConfiguration loadConfiguration(JsonObject object) {
		return new GameConfiguration(object);
	}
	
	@Override
	public void setDefaultKitContentManager(boolean allowDrop) {
		this.kitContentManager = new DefaultKitContentManager(allowDrop);
	}
	
	@Override
	public CustomCreature spawnCustomEntity(Location location, EntityType type) {
		CustomCreatures custom = CustomCreatures.getByType(type);
		
		if(custom != null){
			Class<? extends EntityInsentient> entity = custom.getCustomClass();
			
			try {
				World w = (World) ReflectionUtils.getHandle(location.getWorld());
				
				EntityInsentient e = entity.getConstructor(World.class).newInstance(w);
				e.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
				
				w.addEntity(e, SpawnReason.CUSTOM);
				
				return (CustomCreature) e;

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
		return null;
	}
}

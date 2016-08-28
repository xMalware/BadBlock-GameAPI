package fr.badblock.game.core18R3;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.badblock.game.core18R3.chest.GameChestGenerator;
import fr.badblock.game.core18R3.commands.AdminModeCommand;
import fr.badblock.game.core18R3.commands.BackCommand;
import fr.badblock.game.core18R3.commands.BroadcastCommand;
import fr.badblock.game.core18R3.commands.ClearChatCommand;
import fr.badblock.game.core18R3.commands.ClearInventoryCommand;
import fr.badblock.game.core18R3.commands.CompassCommand;
import fr.badblock.game.core18R3.commands.EnderchestCommand;
import fr.badblock.game.core18R3.commands.FeedCommand;
import fr.badblock.game.core18R3.commands.FlyCommand;
import fr.badblock.game.core18R3.commands.FreezeCommand;
import fr.badblock.game.core18R3.commands.GameModeCommand;
import fr.badblock.game.core18R3.commands.GodmodeCommand;
import fr.badblock.game.core18R3.commands.HealCommand;
import fr.badblock.game.core18R3.commands.I18RCommand;
import fr.badblock.game.core18R3.commands.InvseeCommand;
import fr.badblock.game.core18R3.commands.JumpToCommand;
import fr.badblock.game.core18R3.commands.KickallCommand;
import fr.badblock.game.core18R3.commands.KillallCommand;
import fr.badblock.game.core18R3.commands.KitsCommand;
import fr.badblock.game.core18R3.commands.LagCommand;
import fr.badblock.game.core18R3.commands.ListCommand;
import fr.badblock.game.core18R3.commands.MoreCommand;
import fr.badblock.game.core18R3.commands.PingCommand;
import fr.badblock.game.core18R3.commands.PortalCommand;
import fr.badblock.game.core18R3.commands.RepairCommand;
import fr.badblock.game.core18R3.commands.ShopCommand;
import fr.badblock.game.core18R3.commands.SkullCommand;
import fr.badblock.game.core18R3.commands.SpawnMobCommand;
import fr.badblock.game.core18R3.commands.SpeedCommand;
import fr.badblock.game.core18R3.commands.SudoCommand;
import fr.badblock.game.core18R3.commands.SuicideCommand;
import fr.badblock.game.core18R3.commands.TeleportAllCommand;
import fr.badblock.game.core18R3.commands.TeleportCommand;
import fr.badblock.game.core18R3.commands.UpCommand;
import fr.badblock.game.core18R3.commands.VanishCommand;
import fr.badblock.game.core18R3.commands.WandCommand;
import fr.badblock.game.core18R3.commands.WhitelistCommand;
import fr.badblock.game.core18R3.commands.WorkbrenchCommand;
import fr.badblock.game.core18R3.configuration.GameConfiguration;
import fr.badblock.game.core18R3.entities.CustomCreatures;
import fr.badblock.game.core18R3.fakeentities.FakeEntities;
import fr.badblock.game.core18R3.game.GameServer;
import fr.badblock.game.core18R3.game.GameServerManager;
import fr.badblock.game.core18R3.i18n.GameI18n;
import fr.badblock.game.core18R3.internalutils.TeleportUtils;
import fr.badblock.game.core18R3.itemstack.GameCustomInventory;
import fr.badblock.game.core18R3.itemstack.GameItemExtra;
import fr.badblock.game.core18R3.itemstack.GameItemStackFactory;
import fr.badblock.game.core18R3.itemstack.ItemStackExtras;
import fr.badblock.game.core18R3.jsonconfiguration.APIConfig;
import fr.badblock.game.core18R3.jsonconfiguration.DefaultKitContentManager;
import fr.badblock.game.core18R3.ladder.GameLadderSpeaker;
import fr.badblock.game.core18R3.listeners.ChangeWorldEvent;
import fr.badblock.game.core18R3.listeners.ChatListener;
import fr.badblock.game.core18R3.listeners.DisconnectListener;
import fr.badblock.game.core18R3.listeners.FakeDeathCaller;
import fr.badblock.game.core18R3.listeners.GameServerListener;
import fr.badblock.game.core18R3.listeners.JailedPlayerListener;
import fr.badblock.game.core18R3.listeners.LoginListener;
import fr.badblock.game.core18R3.listeners.MoveListener;
import fr.badblock.game.core18R3.listeners.PlayerInteractListener;
import fr.badblock.game.core18R3.listeners.PortalListener;
import fr.badblock.game.core18R3.listeners.ProjectileHitBlockCaller;
import fr.badblock.game.core18R3.listeners.ShopListener;
import fr.badblock.game.core18R3.listeners.fixs.ArrowBugFixListener;
import fr.badblock.game.core18R3.listeners.fixs.UselessDamageFixListener;
import fr.badblock.game.core18R3.listeners.mapprotector.BlockMapProtectorListener;
import fr.badblock.game.core18R3.listeners.mapprotector.DefaultMapProtector;
import fr.badblock.game.core18R3.listeners.mapprotector.EntityMapProtectorListener;
import fr.badblock.game.core18R3.listeners.mapprotector.PlayerMapProtectorListener;
import fr.badblock.game.core18R3.listeners.packets.CameraListener;
import fr.badblock.game.core18R3.listeners.packets.EquipmentListener;
import fr.badblock.game.core18R3.listeners.packets.InteractEntityListener;
import fr.badblock.game.core18R3.merchant.GameMerchantInventory;
import fr.badblock.game.core18R3.merchant.shops.Merchant;
import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.game.core18R3.packets.GameBadblockOutPacket.GameBadblockOutPackets;
import fr.badblock.game.core18R3.packets.out.GameParticleEffect;
import fr.badblock.game.core18R3.players.GameCustomObjective;
import fr.badblock.game.core18R3.players.GameTeam;
import fr.badblock.game.core18R3.players.data.GameKit;
import fr.badblock.game.core18R3.players.listeners.GameJoinItems;
import fr.badblock.game.core18R3.players.listeners.GameScoreboard;
import fr.badblock.game.core18R3.signs.GameSignManager;
import fr.badblock.game.core18R3.signs.UpdateSignListener;
import fr.badblock.game.core18R3.sql.FakeSQLDatabase;
import fr.badblock.game.core18R3.sql.GameSQLDatabase;
import fr.badblock.game.core18R3.technologies.RabbitSpeaker;
import fr.badblock.game.core18R3.watchers.GameWatchers;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.configuration.BadConfiguration;
import fr.badblock.gameapi.databases.LadderSpeaker;
import fr.badblock.gameapi.databases.SQLDatabase;
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
import fr.badblock.gameapi.portal.Portal;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.gameapi.servers.JoinItems;
import fr.badblock.gameapi.servers.MapProtector;
import fr.badblock.gameapi.signs.SignManager;
import fr.badblock.gameapi.utils.entities.CustomCreature;
import fr.badblock.gameapi.utils.general.JsonUtils;
import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;
import fr.badblock.gameapi.utils.itemstack.ItemStackFactory;
import fr.badblock.gameapi.utils.merchants.CustomMerchantInventory;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.permissions.PermissionManager;
import io.netty.util.internal.ConcurrentSet;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.World;

public class GamePlugin extends GameAPI {
	public static final boolean EMPTY_VERSION = false;

	public static final String FOLDER_I18N 		   = "/home/dev01/i18n",
							   FOLDER_KITS		   = "kits",
							   CONFIG_DATABASES    = "databases.json",
							   WHITELIST		   = "whitelist.yml";
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
	private SQLDatabase					sqlDatabase;
	@Getter
	private LadderSpeaker				ladderDatabase;
	@Getter
	private GameServerManager			gameServerManager;
	@Getter
	private RabbitSpeaker				rabbitSpeaker;

	// Packet system
	@Getter
	private Map<Class<? extends BadblockInPacket>, Set<InPacketListener<?>>>		packetInListeners	= Maps.newConcurrentMap();
	@Getter
	private Map<Class<? extends BadblockOutPacket>, Set<OutPacketListener<?>>>		packetOutListeners	= Maps.newConcurrentMap();

	@Getter
	private List<String>				whitelist;
	@Setter
	private boolean						whitelistStatus = false;

	@Getter
	private Map<String, Merchant>		merchants		= Maps.newConcurrentMap();
	@Getter
	private File						shopFolder		= null;


	@Getter
	private Map<String, Portal>			portals			= Maps.newConcurrentMap();
	private File						portalFolder	= null;

	@Getter
	private SignManager					signManager		= new GameSignManager();
	
	@Getter
	private GameChestGenerator			chestGenerator;
	
	@Getter
	private RunType						runType;
	@Getter@Setter
	private boolean						compassSelectNearestTarget;
	
	@Getter
	private double						serverBadcoinsBonus;
	@Getter
	private double						serverXpBonus;
	
	@Override
	public void onEnable() {
		thread		= Thread.currentThread();
		instance 	= this;
		GameAPI.API = this;

		i18n  		= new GameI18n(); // Cr�ation de l'I18N pour permettre la couleur

		if(!getDataFolder().exists()) getDataFolder().mkdirs();

		try {
			/**
			 * Chargement de la configuration
			 */

			GameAPI.logColor("&b[GameAPI] &aLoading API...");
			long nano = System.nanoTime();

			File configFile  = new File(getDataFolder(), CONFIG_DATABASES);

			if(!configFile.exists())
				configFile.createNewFile();
			APIConfig config = JsonUtils.load(configFile, APIConfig.class);

			loadI18n();

			if(!EMPTY_VERSION) {
				teams 		 	 = Maps.newConcurrentMap();

				GameAPI.logColor("&b[GameAPI] &aLoading databases configuration...");

				if(config == null) {
					config = new APIConfig();
					JsonUtils.save(configFile, config, true);
				}

				runType = config.runType;
				
				GameAPI.logColor("&b[GameAPI] &a=> Ladder : " + config.ladderIp + ":" +  config.ladderPort);
				GameAPI.logColor("&b[GameAPI] &aConnecting to Ladder...");

				new PermissionManager(new JsonArray());
				ladderDatabase   = new GameLadderSpeaker(config.ladderIp, config.ladderPort);
				ladderDatabase.askForPermissions();

				if(!GameAPI.TEST_MODE){
					GameAPI.logColor("&b[GameAPI] &a=> SQL : " + config.sqlIp + ":" +  config.sqlPort);
					GameAPI.logColor("&b[GameAPI] &aConnecting to SQL...");

					sqlDatabase   = new GameSQLDatabase(config.sqlIp, Integer.toString(config.sqlPort), config.sqlUser, config.sqlPassword, config.sqlDatabase);
					((GameSQLDatabase) sqlDatabase).openConnection();
					
					rabbitSpeaker = new RabbitSpeaker(config);
				} else {
					sqlDatabase = new FakeSQLDatabase();
				}
			}

			GameAPI.logColor("&b[GameAPI] &aLoading NMS classes...");
			/**
			 * Chargement des classes NMS
			 */
			CustomCreatures.registerEntities();

			GameAPI.logColor("&b[GameAPI] &aRegistering listeners...");
			/**
			 * Chargement des Listeners
			 */
			new LoginListener(); 				// Met le BadblockPlayer � la connection

			if(!EMPTY_VERSION){
				new DisconnectListener(); 			// G�re la d�connection
				new JailedPlayerListener(); 		// Permet de bien jail les joueurs
				new ItemStackExtras(); 		 	    // Permet de bien g�rer les items sp�ciaux
				new ProjectileHitBlockCaller();		// Permet d'appeler un event lorsque un projectile rencontre un block
				new GameServerListener();			// Permet la gestion des joueurs vers Docker
				new FakeDeathCaller();				// Permet de g�rer les fausses morts et la d�tections du joueur
				new ChangeWorldEvent();				// Permet de mieux g�rer les fausses dimensions
				new PlayerInteractListener();	    // Permet aux administrateurs de d�finir une zone
				new MoveListener();					// Permet d'emp�cher les joueurs de sortir d'une zone
				new ChatListener();					// Permet de formatter le chat
				joinItems = new GameJoinItems();    // Items donn� � l'arriv�e du joueur
				chestGenerator = new GameChestGenerator();
				
				/** Correction de bugs Bukkit */

				new ArrowBugFixListener();			// Permet de corriger un bug avec les fl�ches se lan�ant mal
				new UselessDamageFixListener();		// Enl�ve les d�gats inutiles lors d'une chute
				//new PlayerTeleportFix();			// Permet de bien voir le joueur lorsqu'il respawn (bug Bukkit)

				/** Protection et optimisations par d�faut */

				new PlayerMapProtectorListener();	// Permet de prot�ger la map
				new BlockMapProtectorListener();	// Permet de prot�ger la map
				new EntityMapProtectorListener();	// Permet de prot�ger la map

				/** Packets �cout�s par l'API */
				GameAPI.logColor("&b[GameAPI] &aRegistering packets listeners ...");

				new CameraListener().register();	// Packet pour voir � la place du joueur en spec (aucun event sur Bukkit)
				new InteractEntityListener().register();
				new EquipmentListener().register();
				new UpdateSignListener().register();
				//AntiCheat.load();
				
				getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

				GameAPI.logColor("&b[GameAPI] &aCreating scoreboard...");

				badblockScoreboard = new GameScoreboard(); // Permet de g�rer le scoreboard
			}
			/**
			 * Chargement des commandes par d�faut
			 */
			GameAPI.logColor("&b[GameAPI] &aRegistering commands...");

			if(!EMPTY_VERSION){
				new AdminModeCommand();
				new I18RCommand();

				new FlyCommand();
				new HealCommand();
				new FeedCommand();
				new GameModeCommand();

				new BroadcastCommand();
				new GodmodeCommand();
				new InvseeCommand();
				new KickallCommand();
				new TeleportCommand();
				new VanishCommand();

				new LagCommand();
				new KitsCommand();
			}

			new FreezeCommand();
			new JumpToCommand();
			new SuicideCommand();
			new SudoCommand();
			new TeleportAllCommand();
			new SkullCommand();
			new WhitelistCommand();
			new ClearChatCommand();
			new KillallCommand();
			new UpCommand();
			new WandCommand();
			new CompassCommand();
			new RepairCommand();
			new SpawnMobCommand();
			new ClearInventoryCommand();
			new EnderchestCommand();
			new ListCommand();
			new PingCommand();
			new BackCommand();
			new MoreCommand();
			new WorkbrenchCommand();
			new SpeedCommand();
			new WeatherCommand();

			File whitelistFile 			= new File(getDataFolder(), WHITELIST);
			FileConfiguration whitelist = YamlConfiguration.loadConfiguration(whitelistFile);

			if(!whitelist.contains("whitelist")){
				whitelist.set("whitelist", Arrays.asList("lelann"));
			}

			this.whitelist = new ArrayList<>();

			for(String player : whitelist.getStringList("whitelist"))
				this.whitelist.add(player.toLowerCase());

			whitelist.save(whitelistFile);

			// Set server bonus
			serverXpBonus 		= config.getBonusXp();
			serverBadcoinsBonus = config.getBonusCoins();
			
			// Loading GameServer
			if(!EMPTY_VERSION){
				GameAPI.logColor("&b[GameAPI] &aGameServer loading...");
				// GameServer apr�s tout
				this.gameServer 	   = new GameServer();
				this.gameServerManager = new GameServerManager(config);
				this.getGameServerManager().start();
			}

			nano = System.nanoTime() - nano;

			double ms = (double) nano / 1_000_000d;
			ms = MathsUtils.round(ms, 3);

			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "whitelist off");

			GameAPI.logColor("&b[GameAPI] &aAPI loaded! (" + ms + "ms)");
			
			File plugins = new File("plugins" + File.separator + "apiPlugins");
			plugins.mkdirs();
			Arrays.stream(Bukkit.getPluginManager().loadPlugins(plugins)).forEach(plugin -> Bukkit.getPluginManager().enablePlugin(plugin));
		} catch (Throwable t){
			t.printStackTrace();

			GameAPI.logColor("&c[GameAPI] Error occurred while loading API. See the stack trace. Restarting...");
			Bukkit.shutdown();
		}
	}

	@Override
	public void onDisable(){
		try {
			if(!EMPTY_VERSION && !TEST_MODE)
				((GameSQLDatabase) sqlDatabase).closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(i18n != null)
			i18n.save();
		
		gameServer.cancelReconnectionInvitations();
		
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
	public BadblockOfflinePlayer getOfflinePlayer(@NonNull String name) {
		if(EMPTY_VERSION) return null;

		return EMPTY_VERSION ? null : gameServer.getPlayers().get(name.toLowerCase());
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

		Set<InPacketListener<?>> list = packetInListeners.get(packet);
		if(list == null) {
			list = new ConcurrentSet<>();
		}

		list.add(listener);
		packetInListeners.put(packet, list);
	}

	@Override
	public <T extends BadblockOutPacket> void listenAtPacket(@NonNull OutPacketListener<T> listener) {
		Class<? extends BadblockOutPacket> packet = listener.getGenericPacketClass();

		Set<OutPacketListener<?>> list = packetOutListeners.get(packet);
		if(list == null) {
			list = new ConcurrentSet<>();
		}

		list.add(listener);
		packetOutListeners.put(packet, list);
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
	public FakeEntity<WatcherEntity> spawnFakeFallingBlock(Location location, Material type, byte data) {
		return FakeEntities.spawnFakeFallingBlock(location, type, data);
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

	@Override
	public void manageShops(File folder) {
		if(shopFolder != null)
			throw new IllegalStateException("Merchants are already loaded");

		if(!folder.exists()) 
			folder.mkdirs();

		this.shopFolder = folder;

		for(File file : folder.listFiles()){
			String name = file.getName().toLowerCase().replace(".json", "");
			merchants.put(name, new Merchant(name, loadConfiguration(file)));
		}

		new ShopCommand();
		new ShopListener();
	}

	@Override
	public void managePortals(File folder) {
		if(portalFolder != null)
			throw new IllegalStateException("Portals are already loaded");

		if(!folder.exists())
			folder.mkdirs();

		this.portalFolder = folder;

		// On charge commandes/listeners qu'on avait pas load pour �conomiser sur les serveurs ou inutiles
		new PortalCommand();
		new PortalListener();

		for(File file : folder.listFiles()){
			Portal portal = JsonUtils.load(file, Portal.class);
			String name   = file.getName().split("\\.")[0];

			portal.setFile(file);

			portals.put(name.toLowerCase(), portal);
		}
	}

	public void addPortal(String name, Portal portal){
		File file = new File(portalFolder, name + ".json");
		portal.setFile(file);

		portals.put(name, portal);
	}

	public void savePortal(Portal portal){
		JsonUtils.save(portal.getFile(), portal, true);
	}

	public void removePortal(String name){
		Portal portal = portals.get(name);

		if(portal != null){
			portal.getFile().delete();
			portals.remove(name);
		}
	}

	@Override
	public Portal getPortal(@NonNull String name) {
		return portals.get(name.toLowerCase());
	}

	@Override
	public Portal getPortal(@NonNull Location location) {
		for(Portal portal : portals.values()){
			if(portal.getPortal().isInSelection(location))
				return portal;
		}

		return null;
	}

	@Override
	public Collection<Portal> getLoadedPortals() {
		return Collections.unmodifiableCollection(portals.values());
	}

	@Override
	public Collection<String> getWhitelistedPlayers() {
		return Collections.unmodifiableCollection(whitelist);
	}

	@Override
	public void whitelistPlayer(String player) {
		if(!whitelist.contains(player.toLowerCase())){
			whitelist.add(player.toLowerCase());
		}
	}

	@Override
	public void unwhitelistPlayer(String player) {
		if(whitelist.contains(player.toLowerCase())){
			whitelist.remove(player.toLowerCase());
		}
	}

	@Override
	public boolean isWhitelisted(String player){
		return whitelist.contains(player.toLowerCase());
	}

	@Override
	public boolean getWhitelistStatus() {
		return whitelistStatus;
	}

	@Override
	public Location getSafeLocation(Location location) {
		return TeleportUtils.getSafeDestination(location);
	}
}

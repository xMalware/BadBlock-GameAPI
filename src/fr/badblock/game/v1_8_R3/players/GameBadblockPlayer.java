package fr.badblock.game.v1_8_R3.players;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.game.v1_8_R3.watchers.MetadataIndex;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.events.api.PlayerLoadedEvent;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.game.result.Result;
import fr.badblock.gameapi.packets.BadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayBlockAction;
import fr.badblock.gameapi.packets.out.play.PlayCamera;
import fr.badblock.gameapi.packets.out.play.PlayChangeGameState;
import fr.badblock.gameapi.packets.out.play.PlayChangeGameState.GameState;
import fr.badblock.gameapi.packets.out.play.PlayChat;
import fr.badblock.gameapi.packets.out.play.PlayChat.ChatType;
import fr.badblock.gameapi.packets.out.play.PlayEntityStatus;
import fr.badblock.gameapi.packets.out.play.PlayEntityStatus.EntityStatus;
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
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.utils.CustomObjective;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.i18n.I18n;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.i18n.messages.GameMessages;
import fr.badblock.gameapi.utils.reflection.ReflectionUtils;
import fr.badblock.gameapi.utils.reflection.Reflector;
import fr.badblock.gameapi.utils.selections.Vector3f;
import fr.badblock.permissions.PermissiblePlayer;
import fr.badblock.permissions.PermissionManager;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.ChunkCoordIntPair;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerChunkMap;
import net.minecraft.server.v1_8_R3.WorldServer;

public class GameBadblockPlayer extends CraftPlayer implements BadblockPlayer {
	@Getter@Setter
	private CustomObjective 			 customObjective 	  = null;
	@Getter
	private GamePlayerData 				 playerData 		  = null;
	private PermissiblePlayer 			 permissions 		  = null;

	private Location 					 jail 				  = null;
	private Map<Class<?>, InGameData> 	 inGameData  		  = null;

	private String 						 bossBarMessage 	  = null;
	private FakeEntity<WatcherWither> 	 enderdragon 		  = null;

	private GameMode 					 gamemodeBefJail 	  = null;
	private FakeEntity<?> 				 fakeJailer 		  = null;

	private BukkitRunnable 				 spectatorRunnable 	  = null;
	@Getter
	private BadblockMode 				 badblockMode 		  = BadblockMode.PLAYER;
	@Setter
	private boolean 					 hasJoined 			  = false;
	@Getter@Setter
	private Environment 				 customEnvironment 	  = null;
	@Getter@Setter
	private BadblockTeam				 team				  = null;
	@Setter
	private boolean						 adminMode			  = false;
	
	public GameBadblockPlayer(CraftServer server, EntityPlayer entity, GameOfflinePlayer offlinePlayer) {
		super(server, entity);

		this.inGameData  = Maps.newConcurrentMap();

		this.playerData  = offlinePlayer == null ? new GamePlayerData() : offlinePlayer.getPlayerData(); // On initialise pour ne pas provoquer de NullPointerException, mais sera recréé à la récéptions des données
		this.permissions = PermissionManager.getInstance().createPlayer(getName(), offlinePlayer == null ? new JsonObject() : playerData.getData());

		GameAPI.getAPI().getLadderDatabase().getPlayerData(this, new Callback<JsonObject>() {
			@Override
			public void done(JsonObject result, Throwable error) {
				updateData(result);

				while (!hasJoined)
					try {
						Thread.sleep(10L);
					} catch (InterruptedException unused) {
					}

				synchronized (Bukkit.getServer()) {
					Bukkit.getPluginManager().callEvent(new PlayerLoadedEvent(GameBadblockPlayer.this));
				}
			}
		});
	}

	public void loadInjector() {
		Channel channel = getHandle().playerConnection.networkManager.channel;
		channel.pipeline().addBefore("packet_handler", "api", new BadblockInjector());
	}

	public void updateData(JsonObject object) {
		if (object.has("game")) {
			playerData = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create()
					.fromJson(object.get("game"), GamePlayerData.class);
		}

		if (object.has("permissions")) {
			permissions = PermissionManager.getInstance().createPlayer(getName(), object);
		}
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
		setSaturation(20);
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

	protected Locale getLocale() {
		return playerData.getLocale() == null ? Locale.FRENCH_FRANCE : playerData.getLocale();
	}

	@Override
	public void sendTranslatedMessage(String key, Object... args) {
		sendMessage(getI18n().get(getLocale(), key, args));
	}

	@Override
	public void sendActionBar(String message) {
		message = getI18n().replaceColors(message);
		getAPI().createPacket(PlayChat.class).setType(ChatType.ACTION)
				.setComponents(TextComponent.fromLegacyText(message)).send(this);
	}

	@Override
	public void sendTranslatedActionBar(String key, Object... args) {
		sendActionBar(getI18n().get(getLocale(), key, args)[0]);
	}

	@Override
	public void sendBossBar(String message) {
		message = getI18n().replaceColors(message);

		if (message.length() > 64)
			message = message.substring(0, 63);

		if (message.isEmpty()) {
			removeBossBar();
		} else {
			this.bossBarMessage = message;

			if (enderdragon != null)
				return;

			Location loc = getLocation().clone();
			loc.add(loc.getDirection().multiply(50.0D));

			enderdragon = GameAPI.getAPI().spawnFakeLivingEntity(loc, EntityType.WITHER, WatcherWither.class); // en
																												// vré
																												// cé
																												// un
																												// wither
			enderdragon.getWatchers().setCustomName(message).setCustomNameVisible(true).setInvisibile(true);

			enderdragon.show(this);

			new BossBarRunnable().runTaskTimer(getAPI(), 0, 1L);
		}
	}

	@Override
	public void removeBossBar() {
		if (enderdragon != null)
			enderdragon.remove();

		enderdragon = null;
		bossBarMessage = null;
	}

	@Override
	public void sendTranslatedBossBar(String key, Object... args) {
		sendBossBar(getI18n().get(getLocale(), key, args)[0]);
	}

	@Override
	public void sendTitle(String title, String subtitle) {
		title = getI18n().replaceColors(title);
		subtitle = getI18n().replaceColors(subtitle);

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
	public void showFloatingText(String text, Location location, long lifeTime, double offset) {
		text = getI18n().replaceColors(text);

		if (location == null) {
			location = getEyeLocation().add(getEyeLocation().getDirection().multiply(2.0d));
		}

		location.subtract(0, 1.975, 0); // On enlève la hauteur de l'armor stand

		if (offset != 0)
			location.add(MathsUtils.doubleRandomInclusive(-offset, offset),
					MathsUtils.doubleRandomInclusive(-offset, offset),
					MathsUtils.doubleRandomInclusive(-offset, offset));

		FakeEntity<WatcherArmorStand> entity = GameAPI.getAPI().spawnFakeArmorStand(location);

		entity.getWatchers().setInvisibile(true).setCustomName(text).setCustomNameVisible(true).setOnFire(false);

		entity.updateWatchers();
		entity.show(this);

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
	public void showTranslatedFloatingText(Location location, long lifeTime, double offset, String key,
			Object... args) {
		showFloatingText(getI18n().get(key, args)[0], location, lifeTime, offset);
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
			fakeJailer.show(this);

			getAPI().createPacket(PlayCamera.class).setEntityId(fakeJailer.getId()).send(this);
		}
	}

	@Override
	public boolean isJailed() {
		return jail != null;
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
		getHandle().getDataWatcher().watch(MetadataIndex.ARROW_COUNT.getIndex(), new Byte(amount));
	}

	@Override
	public void changePlayerDimension(Environment world) {
		if(customEnvironment != null && customEnvironment == world) return;
		if(world == getWorld().getEnvironment()) return;

		customEnvironment = world;
		
		getAPI().createPacket(PlayRespawn.class).setDimension(world).setDifficulty(getWorld().getDifficulty())
				.setGameMode(getGameMode()).setWorldType(getWorld().getWorldType()).send(this);

		reloadMap();
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
			Packet<?> nmsPacket = ((GameBadblockOutPacket) packet).buildPacket();
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
		return permission.getPermission() == null ? true : hasPermission(permission.getPermission());
	}

	@Override
	public boolean hasPermission(String permission) {
		return permissions.hasPermission(permission);
	}

	@Override
	public String getGroupPrefix() {
		return permissions.getParent().getDisplayName();
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
		out.writeUTF(getName());
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

				spectatorRunnable = new TooFarRunnable();
				spectatorRunnable.runTaskTimer(GameAPI.getAPI(), 0, 20L);
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

			for (Player online : Bukkit.getOnlinePlayers()) {
				if (online.equals(this) || online.getGameMode() == GameMode.SPECTATOR
						|| !online.getLocation().getWorld().equals(getLocation().getWorld()))
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

				if (tp != null)
					teleport(tp);

				GameMessages.doNotGoTooFarWhenSpectator().send(GameBadblockPlayer.this);
			}
		}

		public Player getRandomNonSpecPlayer() {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.getGameMode() != GameMode.SPECTATOR)
					return player;
			}
			return null;
		}
	}

	class BossBarRunnable extends BukkitRunnable {
		private int time = 10;

		@Override
		public void run() {
			if (bossBarMessage == null || enderdragon == null || !isOnline()) { // message
																				// supprimé
																				// ou
																				// joueur
																				// déco,
																				// on
																				// arrête
				cancel();
				return;
			}

			time--;

			if (time == 0) {
				Location loc = getLocation().clone();
				loc.add(loc.getDirection().multiply(50.0D));

				if (enderdragon.getLocation().distance(loc) > 128.0d) { // trop
																		// loin
																		// (téléportation),
																		// on
																		// recrée
					enderdragon.teleport(loc);
					enderdragon.remove();
					enderdragon.show(GameBadblockPlayer.this);
				} else {
					enderdragon.teleport(loc); // on téléporte l'entité pour
												// qu'elle reste prêt du joueur
				}

				enderdragon.getWatchers().setCustomName(bossBarMessage); // si
																			// le
																			// nom
																			// a
																			// été
																			// update
				enderdragon.updateWatchers(); // on informe le joueur du
												// changement

				time = 10;
			}
		}
	}
	
	@Override
	public boolean hasAdminMode(){
		return adminMode;
	}

	@Override
	public String[] getTranslatedMessage(String key, Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postResult(Result result) {
		// TODO Auto-generated method stub
		
	}
}
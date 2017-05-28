package fr.badblock.bukkit.hub.objects;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.chests.objects.ChestLoader;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.chests.objects.ChestOpener;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.chests.objects.CustomChest;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.chests.objects.CustomChestType;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.parent.MetamorphosisItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.utils.Wings;
import fr.badblock.bukkit.hub.inventories.market.ownitems.OwnableItem;
import fr.badblock.bukkit.hub.inventories.shop.inventories.MiniGameShopInventory;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.players.scoreboard.BadblockScoreboardGenerator;
import fr.badblock.gameapi.utils.ConfigUtils;
import fr.badblock.gameapi.utils.entities.CustomCreature;
import fr.badblock.gameapi.utils.entities.CustomCreature.CreatureBehaviour;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.threading.TaskManager;
import fr.badblock.gameapi.utils.threading.TempScheduler;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HubPlayer implements InGameData {

	public static int a;
	static {
		TaskManager.scheduleSyncRepeatingTask("ladder_updateplayers", new Runnable() {
			@Override
			public void run() {
				GameAPI.getAPI().getLadderDatabase().sendPing(new String[] { "*" }, new Callback<Integer>() {
					@Override
					public void done(Integer arg0, Throwable arg1) {
						a = arg0;
					}
				});
			}
		}, 0, 2);
	}

	public static HubPlayer get(BadblockPlayer player) {
		return player.inGameData(HubPlayer.class);
	}

	public static HubPlayer get(Player player) {
		return get((BadblockPlayer) player);
	}

	// AntiUseSpam
	private long antiSpamClicked;

	private long bigAntiSpamClicked;
	public MountItem clickedMountItem;
	// Inventories
	public CustomInventory currentInventory;
	// Disguise
	public MetamorphosisItem disguise;

	public DyeColor dyeColor;
	private List<String> friends;

	public CustomCreature lastCreature;

	public long lastMount;

	public long lastVipCuboid;
	public long lastSendVipMessage;
	public long lastGetAwaySendMessage;

	public MountItem mounted;

	private BadblockScoreboardGenerator scoreboard;

	// Mount
	public LivingEntity mountEntity;

	public Map<String, Team> teams = new HashMap<>();

	public long teleportMount;

	// Particles
	public List<Effect> particles = new ArrayList<>();
	public ParticleItem clickedParticleItem;

	// Basic informations
	private UUID uuid;

	// data en vrac
	private ChestOpener chestOpener;
	private boolean		chestFreeze;
	private Location	chestFreezeLocation;

	private boolean velocity = false;

	private long timeBetweenEachVelocityUsage;

	public FakeEntity<?> fakeEntity;

	public CustomInventory shopInventory = CustomInventory.get(MiniGameShopInventory.class);

	public OwnableItem buyItem;

	public long lastChat;

	public String message;

	public long lastMove = System.currentTimeMillis() + 300_000L;

	public PlayerBooster lastBooster;

	public HubPlayer() {
		this.setFriends(new ArrayList<>());
	}

	public boolean hasSpam(BadblockPlayer player) {
		long time = System.currentTimeMillis();
		if (this.getBigAntiSpamClicked() >= time)
			return true;
		if (this.getAntiSpamClicked() >= time) {
			player.sendTranslatedMessage("hub.spam.waitbetweeneachinteraction");
			this.setBigAntiSpamClicked(time + 200);
			return true;
		}
		use();
		return false;
	}

	public void updateScoreboard() {
		this.getScoreboard().generate();
	}

	public void lodad(BadblockPlayer player) {
		BadBlockHub hub = BadBlockHub.getInstance();
		final TempScheduler tempScheduler0 = new TempScheduler();
		tempScheduler0.task = TaskManager.scheduleSyncRepeatingTask("hub_" + player.getName() + "_" + player.getEntityId(), new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (player == null || !player.isOnline()) {
					TaskManager.taskList.remove(tempScheduler0.task.getTaskId());
					tempScheduler0.task.cancel();
					return;
				}
				if (lastMove < System.currentTimeMillis()) {
					/*if (player.getPlayerData().getBadcoins() <= 0 && player.getPlayerData().getLevel() <= 1 && player.getPlayerData().getXp() <= 0) {
						player.kickPlayer("§cVous êtes resté trop longtemps inactif sur le hub.");
					}*/
				}
				if (getLastVipCuboid() != -1 && getLastVipCuboid() < System.currentTimeMillis()) {
					setLastVipCuboid(-1);
					hub.getNpcxMalware().setHeadYaw(hub.getNpcxMalwareNormal().getYaw());
					hub.getNpcLeLanN().setHeadYaw(hub.getNpcLeLanNNormal().getYaw());
					hub.getVipPortalCuboid().getBlocks().parallelStream()
					.filter(block -> block.getType().equals(Material.AIR))
					.forEach(block -> player.sendBlockChange(block.getLocation(), Material.AIR, (byte) 0));
				}
				// Vérification du dernier coffre gratuit
				for (CustomChestType chestType : ChestLoader.getInstance().getChests()) {
					if (chestType.getGiveEachSeconds() <= -1) continue;
					HubStoredPlayer hubStoredPlayer = HubStoredPlayer.get(player);
					boolean has = false;
					for (CustomChest customChest : hubStoredPlayer.getChests())
						if (customChest.getTypeId() == chestType.getId() && !customChest.isOpened()) 
							has = true;
					if (has) continue;
					if (!hubStoredPlayer.getLastGivenChests().containsKey(chestType.getId()) || (hubStoredPlayer.getLastGivenChests().containsKey(chestType.getId()) && hubStoredPlayer.getLastGivenChests().get(chestType.getId()) + (chestType.getGiveEachSeconds() * 1000L) < System.currentTimeMillis())) {
						hubStoredPlayer.getLastGivenChests().put(chestType.getId(), System.currentTimeMillis());
						hubStoredPlayer.getChests().add(new CustomChest(chestType.getId(), false));
						player.sendTranslatedMessage("hub.chests.youhavereceivednewchest", player.getTranslatedMessage("hub.chests." + chestType.getId() + ".name")[0]);
						player.saveGameData();
					}
				}
				GameAPI.getAPI().getSqlDatabase().call("SELECT id, xp, badcoins FROM debts WHERE playerName = '" + player.getName().toLowerCase() + "'", SQLRequestType.QUERY, new Callback<ResultSet>() {

					@Override
					public void done(ResultSet result, Throwable error) {
						try {
							boolean a = false;
							while (result.next()) {
								GameAPI.getAPI().getSqlDatabase().createStatement().executeUpdate("DELETE FROM debts WHERE id = '" + result.getLong("id") + "'");	
								if (result.getLong("badcoins") > 0) {
									player.getPlayerData().addBadcoins(result.getInt("badcoins"), false);
								}
								if (result.getLong("xp") > 0) {
									player.getPlayerData().addXp(result.getInt("xp"), false);
									a = true;
								}
							}
							if (a) {
								player.saveGameData();
								if (player.getCustomObjective() != null)
									player.getCustomObjective().generate();

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});
			}
		}, 1, 5 * 300);
		HubStoredPlayer hubStoredPlayer = HubStoredPlayer.get(player);
		if (hubStoredPlayer.getMountConfigs() == null)
			hubStoredPlayer.setMountConfigs(new TreeMap<>());
		if (hubStoredPlayer.getParticleConfigs() == null)
			hubStoredPlayer.setParticleConfigs(new TreeMap<>());
		TempScheduler tempScheduler2 = new TempScheduler();
		tempScheduler2.task = TaskManager.scheduleSyncRepeatingTask("hub_show_" + player.getName() + "_" + player.getEntityId(), new Runnable() {
			/*int length = 0;
			long time = 0;
			int id = 0;
			boolean iv;*/
			@Override
			public void run() {
				if (player == null || !player.isOnline()) {
					TaskManager.taskList.remove(tempScheduler2.task.getTaskId());
					tempScheduler2.task.cancel();
					return;
				}
				BadblockPlayer bbPlayer = player;
				/*List<String> list = Arrays.asList(GameAPI.i18n().get(player.getPlayerData().getLocale(), "hub.actionbar"));
				if (id > list.size() - 1) id = 0;
				String actionBar = list.get(id).substring(2, list.get(id).length() - 1);
				if (length <= 0) {
					id++;
					length = 0;
					iv = false;
				}else if (length == actionBar.length() / 2) {
					iv = true;
					time = System.currentTimeMillis() + 1500;
				}
				System.out.println(length + "/" + (actionBar.length() / 2));
				if (time < System.currentTimeMillis() && iv) {
					length--;
					iv = true;
				}else length++;
				String character = list.get(id).substring(0, 2) + actionBar.substring(actionBar.length() / 2 - length, actionBar.length() / 2 + (length / 2));
				bbPlayer.sendActionBar(character);*/
				String finalString = "";
				Iterator<String> iterator = Arrays
						.asList(GameAPI.i18n().get(bbPlayer.getPlayerData().getLocale(), "hub.tablist.header"))
						.iterator();
				while (iterator.hasNext()) {
					finalString += iterator.next() + (iterator.hasNext() ? System.lineSeparator() : "");
				}
				String finalString2 = "";
				iterator = Arrays.asList(GameAPI.i18n().get(bbPlayer.getPlayerData().getLocale(), "hub.tablist.footer"))
						.iterator();
				while (iterator.hasNext()) {
					finalString2 += iterator.next() + (iterator.hasNext() ? System.lineSeparator() : "");
				}
				bbPlayer.sendTabHeader(finalString.replace("%0", Integer.toString(a)).replace("%1", player.getName()),
						finalString2.replace("%0", Integer.toString(a)).replace("%1", player.getName()));
				//String finalString3 = "";
				/*iterator = Arrays.asList(player.getTranslatedMessage("hub.bossbar")).iterator();
				while (iterator.hasNext()) {
					finalString3 += iterator.next() + (iterator.hasNext() ? System.lineSeparator() : "");
				}
				bbPlayer.sendBossBar(finalString3.replace("%0", Integer.toString(a)).replace("%1", player.getName()));*/
			}
		}, 0, 10);
		TaskManager.runAsyncTaskLater(new Runnable() {
			@Override
			public void run() {
				while (!player.isDataFetch()) {
					if (!player.isOnline()) return;
				}
				GameAPI.getAPI().getLadderDatabase().getPlayerData(player, new Callback<JsonObject>() {

					@Override
					public void done(JsonObject result, Throwable error) {
						if (result.has("leaves")) {
							JsonArray leaves = result.get("leaves").getAsJsonArray();
							List<Long> leavess = GameAPI.getGson().fromJson(leaves, GameBadblockPlayer.collectType);
							player.setLeaves(leavess);
						}
					}

				});
			}
		}, 5);
		TaskManager.scheduleAsyncRepeatingTask(player.getName() + "_objective", new Runnable() {
			@Override
			public void run() {
				if (player.getCustomObjective() != null)
					player.getCustomObjective().generate();
			}
		}, 2, 2);
		TempScheduler tempScheduler3 = new TempScheduler();
		tempScheduler3.task = TaskManager.scheduleSyncRepeatingTask(player.getName() + "_funmode", new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (player == null || !player.isOnline()) {
					TaskManager.taskList.remove(tempScheduler3.task.getTaskId());
					tempScheduler3.task.cancel();
					return;
				}
				if (mountEntity != null && mountEntity.isValid()) {
					HubPlayer hubPlayer = HubPlayer.get(player);
					MountItem mountItem = hubPlayer.getClickedMountItem();
					if (mountItem == null)
						return;
					HubStoredPlayer hubStoredPlayer = HubStoredPlayer.get(player);
					if (hubPlayer.getFakeEntity() != null) {
						if (hubPlayer.getFakeEntity().getLocation().distanceSquared(player.getLocation()) >= 15) {
							hubPlayer.getFakeEntity().teleport(player.getLocation());
							hubPlayer.getFakeEntity().move(player.getLocation().clone().add(0, 1, 0));
						}
					}
					if (hubPlayer.getLastCreature() != null) {
						if (hubStoredPlayer.getMountConfigs().containsKey(mountItem.getName())) {
							if (!hubStoredPlayer.getMountConfigs().get(mountItem.getName()).isBaby()) {
								if (player.getVehicle() == null
										|| (player.getVehicle() != null && !player.getVehicle().isValid())) {
									mountEntity.remove();
								}
								if (hubStoredPlayer.getMountConfigs().get(mountItem.getName()).isPegasus()
										&& mountItem.hasPegasusMode()) {
									if (!mountEntity.isOnGround())
										Wings.SpawnWings2(mountEntity, hubPlayer, player);
									hubPlayer.getLastCreature().setCreatureBehaviour(CreatureBehaviour.FLYING);
								} else
									hubPlayer.getLastCreature().setCreatureBehaviour(CreatureBehaviour.NORMAL);
								if (hubStoredPlayer.getMountConfigs().get(mountItem.getName()).isReverse()) {
									mountEntity.setCustomName("Dinnerbone");
									mountEntity.setCustomNameVisible(false);
								} else {
									mountEntity.setCustomName(player.getName());
									mountEntity.setCustomNameVisible(false);
								}
							} else {
								hubPlayer.getLastCreature().setCreatureBehaviour(CreatureBehaviour.NORMAL);
							}
							if (mountEntity.isOnGround()) {
								if (mountItem.hasFunMode()) {
									if (hubStoredPlayer.getMountConfigs().containsKey(mountItem.getName())) {
										if (hubStoredPlayer.getMountConfigs().get(mountItem.getName()).isFunMode()) {
											Location location = mountEntity.getLocation().clone();
											SecureRandom secureRandom = new SecureRandom();
											for (int x = -2; x < 2; x++) {
												for (int z = -2; z < 2; z++) {
													Block block = location.clone().add(x, -1, z).getBlock();
													if (block.getType().equals(Material.AIR))
														continue;
													if (block.getType().name().contains("SLAB")
															|| block.getType().name().contains("STAIR")
															|| block.getType().name().contains("STEP"))
														continue;
													for (Player p : Bukkit.getOnlinePlayers())
														p.sendBlockChange(location.clone().add(x, -1, z),
																Material.STAINED_CLAY, (byte) secureRandom.nextInt(16));
													final int finalX = x;
													final int finalZ = z;
													TaskManager.runTaskLater("removefunmode_" + UUID.randomUUID().toString(),
															new Runnable() {
														@Override
														public void run() {
															for (Player p : Bukkit.getOnlinePlayers())
																p.sendBlockChange(
																		location.clone().add(finalX, -1,
																				finalZ),
																		block.getType(), block.getData());
														}
													}, 40);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}, 1, 20);
	}

	public static void t() {

	}

	public void use() {
		long time = System.currentTimeMillis();
		this.setAntiSpamClicked(time + 500);
		this.setBigAntiSpamClicked(time + 200);
	}

	public void justGetVelocity() {
		velocity = true;
	}

	public boolean canChat(boolean hasPermission) {
		if (hasPermission) return hasPermission;
		boolean can = lastChat < System.currentTimeMillis();
		if (can) lastChat = System.currentTimeMillis() + ConfigUtils.getInt(BadBlockHub.getInstance(), "timebetweenmessage");
		return can;
	}

}

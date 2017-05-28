package fr.badblock.bukkit.hub.tasks;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.npc.NPCData;
import fr.badblock.bukkit.hub.rabbitmq.listeners.SEntryInfosListener;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.ConfigUtils;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.threading.TaskManager;
import fr.badblock.sentry.FullSEntry;
import net.md_5.bungee.api.ChatColor;

public class RequestNPCTask extends CustomTask {

	private static final Type type = new TypeToken<Map<Integer, NPCData>>() {}.getType();
	public static boolean work;

	public RequestNPCTask() {
		super(0, 20);
	}

	@Override
	public void done() {
		work();
	}

	public static void work() {
		GameAPI.getAPI().getSqlDatabase().call("SELECT value FROM keyValues WHERE `key` = 'npc'", SQLRequestType.QUERY,
				new Callback<ResultSet>() {

					@Override
					public void done(ResultSet result, Throwable error) {
						try {
							result.next();
							String value = result.getString("value");
							BadBlockHub hub = BadBlockHub.getInstance();
							Gson gson = hub.getGsonExpose();
							Map<Integer, NPCData> updatedMap = gson.fromJson(value, type);
							Map<Integer, NPCData> oldMap = NPCData.stockage;
							TaskManager.runTask(new Runnable() {
								@Override
								public void run() {
									// New
									for (Entry<Integer, NPCData> npcData : updatedMap.entrySet()) {
										if (!oldMap.containsKey(npcData.getKey())) {
											npcData.getValue().yop();
											Location loc = ConfigUtils.convertStringToLocation(npcData.getValue().getLocation()).clone();
											loc.setY(loc.getY() + 5);
											int nb = 0;
											FullSEntry sentry = SEntryInfosListener.sentries.get(npcData.getValue().getServer());
											if (sentry != null) nb = sentry.getIngamePLayers();
											for (BadblockPlayer player : GameAPI.getAPI().getRealOnlinePlayers()) {
												player.showFloatingText(ChatColor.translateAlternateColorCodes('°', npcData.getValue().getDisplayName()), loc, 20, 0);
												Location locA = loc.clone();
												locA.setY(locA.getY() - 1);
												player.showFloatingText("§b" + nb + " §7joueurs dans ce jeu", locA, 20, 0);
											}
										} else {
											npcData.getValue().setFakeEntity(oldMap.get(npcData.getKey()).getFakeEntity());
											Location loc = ConfigUtils.convertStringToLocation(npcData.getValue().getLocation()).clone();
											loc.setY(loc.getY() + 5);
											int nb = 0;
											FullSEntry sentry = SEntryInfosListener.sentries.get(npcData.getValue().getServer());
											if (sentry != null) nb = sentry.getIngamePLayers();
											for (BadblockPlayer player : GameAPI.getAPI().getRealOnlinePlayers()) {
												player.showFloatingText(ChatColor.translateAlternateColorCodes('°', npcData.getValue().getDisplayName()), loc, 20, 0);
												Location locA = loc.clone();
												locA.setY(locA.getY() - 1);
												player.showFloatingText("§b" + nb + " §7joueurs dans ce jeu", locA, 20, 0);
											}
										}
									}
									// Remove old
									for (Entry<Integer, NPCData> npcData : oldMap.entrySet()) {
										if (!updatedMap.containsKey(npcData.getKey())) {
											npcData.getValue().remove();
										}
									}
									NPCData.stockage = updatedMap;
									work = true;
								}
							});
							result.close(); // don't forget to close it
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}

				});
	}

}

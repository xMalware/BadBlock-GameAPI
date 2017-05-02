package fr.badblock.bukkit.hub.tasks;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.npc.NPCData;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.databases.SQLRequestType;
import fr.badblock.gameapi.utils.general.Callback;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class RequestNPCTask extends CustomTask {

	private static final Type type = new TypeToken<Map<Integer, NPCData>>() {}.getType();
	public static boolean work;

	public RequestNPCTask() {
		super(0, 100);
	}

	@Override
	public void done() {
		work();
	}

	public static void work() {
		/*GameAPI.getAPI().getSqlDatabase().call("SELECT value FROM keyValues WHERE `key` = 'npc'", SQLRequestType.QUERY,
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
										} else {
											npcData.getValue().setFakeEntity(oldMap.get(npcData.getKey()).getFakeEntity());
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

				});*/
	}

}

package fr.badblock.bukkit.hub.rabbitmq;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.mojang.authlib.properties.PropertyMap;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.listeners.PlayerMoveListener;
import fr.badblock.bukkit.hub.utils.NPC;
import fr.badblock.gameapi.utils.threading.TaskManager;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;
import fr.badblock.sentry.FullSEntry;

public class SEntryInfosListener extends RabbitListener {

	public static ConcurrentMap<String, FullSEntry> sentries = Maps.newConcurrentMap();
	public static ConcurrentMap<String, Long> tempPlayers = Maps.newConcurrentMap();
	public static ConcurrentMap<String, UUID> tempPlayersUUID = Maps.newConcurrentMap();
	public static ConcurrentMap<String, PropertyMap> tempPlayersPropertyMap = Maps.newConcurrentMap();
	public static ConcurrentMap<String, NPC> tempNPCs = Maps.newConcurrentMap();
	public static ConcurrentMap<String, String> tempPlayersRank = Maps.newConcurrentMap();
	public static long	tempTime	= (long) (600_000L * BungeeWorkerListener.bungeeWorkers);

	public SEntryInfosListener() {
		super(BadBlockHub.getInstance().getRabbitService(), "networkdocker.sentry.infos", false,
				RabbitListenerType.SUBSCRIBER);
		TaskManager.scheduleSyncRepeatingTask("scotch", new Runnable() {
			@Override
			public void run() {
				tempTime	= (long) (600_000L * BungeeWorkerListener.bungeeWorkers);
				Iterator<Entry<String, Long>> iterator = tempPlayers.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, Long> entry = iterator.next();
					if (entry.getValue() < System.currentTimeMillis()) {
						if (tempNPCs.containsKey(entry.getKey())) {
							NPC npc = tempNPCs.get(entry.getKey());
							npc.despawn();
							tempNPCs.remove(entry.getKey());
						}
						tempPlayersUUID.remove(entry.getKey());
						iterator.remove();
					}
					else {
						if (!isCached()) {
							if (tempNPCs.containsKey(entry.getKey())) {
								NPC npc = tempNPCs.get(entry.getKey());
								npc.despawn();
								tempNPCs.remove(entry.getKey());
							}
							tempPlayersUUID.remove(entry.getKey());
							iterator.remove();
						}else{
							if (!tempNPCs.containsKey(entry.getKey()) && tempPlayersUUID.containsKey(entry.getKey()) && tempPlayersRank.containsKey(entry.getKey()) && tempPlayersPropertyMap.containsKey(entry.getKey())) {
								Player pl = Bukkit.getPlayer(entry.getKey());
								if (pl == null || (pl != null && !pl.isOnline())) {
									NPC npc = new NPC(tempPlayersRank.get(entry.getKey()), PlayerMoveListener.spawn);
									/*
									 * 
									if (groups.get(e.getPlayer().getMainGroup()) != null) {
										getHandler().getTeam( groups.get(e.getPlayer().getMainGroup()) ).addEntry(e.getPlayer().getName());
									}
									 */
									for (Player player : Bukkit.getOnlinePlayers())
										if (!player.hasPermission("vip"))
											npc.show(entry.getKey(), tempPlayersUUID.get(entry.getKey()), player, tempPlayersPropertyMap.get(entry.getKey()));
									tempNPCs.put(entry.getKey(), npc);
								}
							}
						}
					}
				}
			}
		}, 1, 1);
	}

	public boolean isCached() {
		return BungeeWorkerListener.bungeeWorkers != 1;
	}

	@Override
	public void onPacketReceiving(String body) {
		if (body == null)
			return;
		FullSEntry sentry = BadBlockHub.getInstance().getGson().fromJson(body, FullSEntry.class);
		if (sentry == null)
			return;
		sentries.put(sentry.getPrefix(), sentry);
	}

}

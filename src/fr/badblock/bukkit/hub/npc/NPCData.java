package fr.badblock.bukkit.hub.npc;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.bukkit.hub.utils.MountManager;
import fr.badblock.gameapi.fakeentities.FakeEntity;
import fr.badblock.gameapi.packets.watchers.WatcherLivingEntity;
import fr.badblock.gameapi.packets.watchers.WatcherZombie;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.ConfigUtils;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.rabbitconnector.RabbitService;
import fr.badblock.sentry.SEntry;
import fr.badblock.utils.Encodage;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

@Getter
@Setter
public class NPCData {

	public static Map<Integer, NPCData> stockage = new HashMap<>();

	@Expose
	private String displayName;
	@Expose
	private EntityType entityType;
	private Inventory inventory;
	@Expose
	private String location;
	@Expose
	private Material highlightedItem;
	@Expose
	private DyeColor dyeColor;
	@Expose
	private String server;
	@Expose
	private boolean vip;
	@Expose
	private boolean staff;
	@Expose
	private boolean matchmaking;
	private FakeEntity<? extends WatcherLivingEntity> fakeEntity;

	public NPCData(String location, EntityType entityType, boolean vip, boolean staff, boolean matchmaking,
			String displayName, String server) {
		this.setLocation(location);
		this.setEntityType(entityType);
		this.setDisplayName(displayName);
		this.setVip(vip);
		this.setStaff(staff);
		this.setMatchmaking(matchmaking);
		this.setServer(server);
		this.yop();
	}

	public void yop() {
		this.setFakeEntity(MountManager.spawn(ConfigUtils.convertStringToLocation(location), entityType, WatcherZombie.class, false, false, false, false, ChatColor.translateAlternateColorCodes('°', displayName)));
		Bukkit.getConsoleSender().sendMessage("[BadBlockHub] Spawned FakeEntity: " + ChatColor.translateAlternateColorCodes('°', displayName));
	}

	public void onClick(BadblockPlayer player) {
		HubPlayer hubPlayer = HubPlayer.get(player);
		// Antispam
		if (hubPlayer.hasSpam(player))
			return;
		// Permission
		if (vip && !player.hasPermission("hub.npc.vip")) {
			player.sendTranslatedMessage("hub.npc.youmustbevip");
			return;
		}
		if (staff && !player.hasPermission("hub.npc.staff")) {
			player.sendTranslatedMessage("hub.npc.youmustbestaff");
			return;
		}
		// Serveur direct, pas de matchmaking
		if (!matchmaking) {
			player.sendPlayer(this.getServer());
			return;
		}
		// Envoi vers le matchmaking
		BadBlockHub instance = BadBlockHub.getInstance();
		RabbitService service = instance.getRabbitService();
		Gson gson = instance.getGson();
		service.sendAsyncPacket("networkdocker.sentry.join", gson.toJson(new SEntry(player.getName(), this.getServer(), false)),
				Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, true);
	}

	public void remove() {
		if (fakeEntity != null) {
			fakeEntity.remove();
			fakeEntity.destroy();
		}
	}

}

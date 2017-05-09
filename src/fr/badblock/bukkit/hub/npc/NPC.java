package fr.badblock.bukkit.hub.npc;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import com.mojang.authlib.GameProfile;

import fr.badblock.gameapi.players.BadblockPlayer;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class NPC {

	private UUID uuid;
	private String name;
	public static HashMap<Integer, NPC> npcs = new HashMap<>();
	public HashMap<UUID, EntityPlayer> forPlayers = new HashMap<>();

	public NPC(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
		System.out.println("New NPC(" + name + ", " + uuid.toString() + ")");
	}

	public EntityPlayer spawn(Location location) {
		System.out.println("Spawn NPC(" + name + ", " + uuid.toString() + ")");
		MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
		EntityPlayer entityPlayer = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(uuid, name),
				new PlayerInteractManager(nmsWorld));
		entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(),
				location.getPitch());
		entityPlayer.spawnIn(nmsWorld);
		npcs.put(entityPlayer.getId(), this);
		return entityPlayer;
	}

	public void despawn(EntityPlayer entityPlayer) {
		npcs.remove(this);
		entityPlayer.die();
	}

	public void show(BadblockPlayer player, Location location) {
		System.out.println("Show NPC(" + name + ", " + uuid.toString() + ")");
		EntityPlayer npc;
		if (!forPlayers.containsKey(player.getUniqueId())) {
			npc = spawn(location);
			forPlayers.put(player.getUniqueId(), npc);
			PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
			connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
					new EntityPlayer[] { npc }));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
		} else {
			npc = forPlayers.get(player.getUniqueId());
			npc.setLocation(location.getX(), location.getY(), location.getX(), location.getYaw(), location.getPitch());
		}
	}

}

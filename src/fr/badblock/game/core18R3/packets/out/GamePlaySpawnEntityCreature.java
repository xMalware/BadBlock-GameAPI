package fr.badblock.game.core18R3.packets.out;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.game.core18R3.watchers.GameWatcherEntity;
import fr.badblock.gameapi.packets.out.play.PlaySpawnEntityCreature;
import fr.badblock.gameapi.packets.watchers.WatcherEntity;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlaySpawnEntityCreature extends GameBadblockOutPacket implements PlaySpawnEntityCreature {
	private int entityId = 0;
	private EntityType type = EntityType.BAT;
	private Location location = null;
	private float headRotation = 0;
	private Vector velocity = null;
	private WatcherEntity watchers = null;
	private DataWatcher tempWatcher = null;

	public GamePlaySpawnEntityCreature(PacketPlayOutSpawnEntityLiving packet) {
		Reflector reflector = new Reflector(packet);

		try {
			entityId = (int) reflector.getFieldValue("a");
			type = get((byte) reflector.getFieldValue("b"));
			location = new Location(Bukkit.getWorlds().get(0), (double) reflector.getFieldValue("c") / 32.0D,
					(double) reflector.getFieldValue("d") / 32.0D, (double) reflector.getFieldValue("e") / 32.0D,
					(float) reflector.getFieldValue("i") / 256.0F * 360.0F,
					(float) reflector.getFieldValue("j") / 256.0F * 360.0F);
			velocity = new Vector((double) reflector.getFieldValue("f") / 8000.0D,
					(double) reflector.getFieldValue("g") / 8000.0D, (double) reflector.getFieldValue("h") / 8000.0D);
			headRotation = (float) reflector.getFieldValue("k") / 256.0F * 360.0F;
			tempWatcher = (DataWatcher) reflector.getFieldValue("l");
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving();
		Reflector reflector = new Reflector(packet);

		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", type.getTypeId());
		reflector.setFieldValue("c", MathHelper.floor(location.getX() * 32.0D));
		reflector.setFieldValue("d", MathHelper.floor(location.getY() * 32.0D));
		reflector.setFieldValue("e", MathHelper.floor(location.getZ() * 32.0D));

		if (velocity == null)
			velocity = new Vector(0f, 0f, 0f);
		reflector.setFieldValue("f", (int) (velocity.getX() * 8000.0D));
		reflector.setFieldValue("g", (int) (velocity.getX() * 8000.0D));
		reflector.setFieldValue("h", (int) (velocity.getX() * 8000.0D));
		reflector.setFieldValue("i", ((byte) (int) (location.getYaw() * 256.0F / 360.0F)));
		reflector.setFieldValue("j", ((byte) (int) (location.getPitch() * 256.0F / 360.0F)));
		reflector.setFieldValue("k", ((byte) (int) ((headRotation * 256.0F / 360.0F))));

		if (watchers != null)
			reflector.setFieldValue("l", ((GameWatcherEntity) watchers).convertToDatawatcher());
		else
			reflector.setFieldValue("l", tempWatcher);

		return packet;
	}

	@SuppressWarnings("deprecation")
	public EntityType get(int id) {
		for (EntityType type : EntityType.values())
			if (type.getTypeId() == id)
				return type;
		return null;
	}
}

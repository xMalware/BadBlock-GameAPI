package fr.badblock.game.core18R3.packets.out;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayWorldParticles;
import fr.badblock.gameapi.particles.ParticleData;
import fr.badblock.gameapi.particles.ParticleEffect;
import fr.badblock.gameapi.particles.ParticleEffectType;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayWorldParticles extends GameBadblockOutPacket implements PlayWorldParticles {
	private static boolean isWater(Location location) {
		Material material = location.getBlock().getType();
		return material == Material.WATER || material == Material.STATIONARY_WATER;
	}
	private ParticleEffect particle;

	private Location location;

	public GamePlayWorldParticles(PacketPlayOutWorldParticles packet) {
		Reflector reflector = new Reflector(packet);

		try {
			particle = new GameParticleEffect(
					ParticleEffectType.valueOf(((EnumParticle) reflector.getFieldValue("a")).name()))
							.setAmount((int) reflector.getFieldValue("i"))
							.setLongDistance((boolean) reflector.getFieldValue("j"))
							.setSpeed((float) reflector.getFieldValue("h"))
							.setOffset(new Vector((float) reflector.getFieldValue("e"),
									(float) reflector.getFieldValue("f"), (float) reflector.getFieldValue("g")))
							.setData(ParticleData.load((int[]) reflector.getFieldValue("k")));
			location = new Location(Bukkit.getWorlds().get(0), (double) reflector.getFieldValue("b"),
					(double) reflector.getFieldValue("c"), (double) reflector.getFieldValue("d"));

		} catch (Exception e) {
		}
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		EnumParticle type = EnumParticle.valueOf(particle.getType().name());
		float x = (float) location.getX();
		float y = (float) location.getY();
		float z = (float) location.getZ();

		float offsetX = particle.getOffset() != null ? (float) particle.getOffset().getX() : 0f;
		float offsetY = particle.getOffset() != null ? (float) particle.getOffset().getY() : 0f;
		float offsetZ = particle.getOffset() != null ? (float) particle.getOffset().getZ() : 0f;

		float speed = particle.getSpeed();
		int amount = particle.getAmount();

		int[] data = particle.getData() != null ? particle.getData().getPacketData() : new int[0];

		if (particle.getType().getData() != null && data.length == 0) {
			new IllegalArgumentException("Need data for " + particle.getType());
		} else if (particle.getType().getData() != null
				&& !particle.getType().getData().equals(particle.getData().getClass())) {
			new IllegalArgumentException("Bad data type for " + particle.getType() + " (need "
					+ particle.getType().getData().getName() + ")");
		}

		if (particle.getType().isNeedWater() && !isWater(location)) {
			new IllegalArgumentException("Need a water block for " + particle.getType() + " !");
		}

		return new PacketPlayOutWorldParticles(type, particle.isLongDistance(), x, y, z, offsetX, offsetY, offsetZ,
				speed, amount, data);
	}
}

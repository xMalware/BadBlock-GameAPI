package fr.badblock.bukkit.hub.effectlib.effect;

import org.bukkit.Location;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.EffectManager;
import fr.badblock.bukkit.hub.effectlib.EffectType;
import fr.badblock.bukkit.hub.effectlib.util.ParticleEffect;

public class HelixEffect extends Effect {

	/**
	 * Factor for the curves. Negative values reverse rotation.
	 */
	public float curve = 10;

	/**
	 * Particle to form the helix
	 */
	public ParticleEffect particle = ParticleEffect.FLAME;

	/**
	 * Particles per strand
	 */
	public int particles = 80;

	/**
	 * Radius of helix
	 */
	public float radius = 10;

	/**
	 * Rotation of the helix (Fraction of PI)
	 */
	public double rotation = Math.PI / 4;

	/**
	 * Amount of strands
	 */
	public int strands = 8;

	public HelixEffect(EffectManager effectManager) {
		super(effectManager);
		type = EffectType.REPEATING;
		period = 10;
		iterations = 8;
	}

	@Override
	public void onRun() {
		Location location = getLocation();
		for (int i = 1; i <= strands; i++) {
			for (int j = 1; j <= particles; j++) {
				float ratio = (float) j / particles;
				double angle = curve * ratio * 2 * Math.PI / strands + (2 * Math.PI * i / strands) + rotation;
				double x = Math.cos(angle) * ratio * radius;
				double z = Math.sin(angle) * ratio * radius;
				location.add(x, 0, z);
				display(particle, location);
				location.subtract(x, 0, z);
			}
		}
	}

}

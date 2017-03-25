package fr.badblock.bukkit.hub.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.EffectManager;
import fr.badblock.bukkit.hub.effectlib.EffectType;
import fr.badblock.bukkit.hub.effectlib.util.ParticleEffect;

public class ArcEffect extends Effect {

	/**
	 * Height of the arc in blocks
	 */
	public float height = 2;

	/**
	 * ParticleType of spawned particle
	 */
	public ParticleEffect particle = ParticleEffect.FLAME;

	/**
	 * Particles per arc
	 */
	public int particles = 100;

	/**
	 * Internal counter
	 */
	protected int step = 0;

	public ArcEffect(EffectManager effectManager) {
		super(effectManager);
		type = EffectType.REPEATING;
		period = 1;
		iterations = 200;
	}

	@Override
	public void onRun() {
		Location location = getLocation();
		Location target = getTarget();
		if (target == null) {
			cancel();
			return;
		}
		Vector link = target.toVector().subtract(location.toVector());
		float length = (float) link.length();
		float pitch = (float) (4 * height / Math.pow(length, 2));
		for (int i = 0; i < particles; i++) {
			Vector v = link.clone().normalize().multiply(length * i / particles);
			float x = ((float) i / particles) * length - length / 2;
			float y = (float) (-pitch * Math.pow(x, 2) + height);
			location.add(v).add(0, y, 0);
			display(particle, location);
			location.subtract(0, y, 0).subtract(v);

			step++;
		}
	}

}

package fr.badblock.bukkit.hub.effectlib.effect;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.EffectManager;
import fr.badblock.bukkit.hub.effectlib.EffectType;
import fr.badblock.bukkit.hub.effectlib.util.MathUtils;
import fr.badblock.bukkit.hub.effectlib.util.ParticleEffect;
import fr.badblock.bukkit.hub.effectlib.util.RandomUtils;
import fr.badblock.bukkit.hub.effectlib.util.VectorUtils;

public class DragonEffect extends Effect {

	/**
	 * Arcs to build the breath
	 */
	public int arcs = 20;
	/**
	 * Length in blocks
	 */
	public float length = 4;
	/**
	 * ParticleType of spawned particle
	 */
	public ParticleEffect particle = ParticleEffect.FLAME;
	/**
	 * Particles per arc
	 */
	public int particles = 30;
	/**
	 * Pitch of the dragon arc
	 */
	public float pitch = .1f;
	protected final List<Double> rndAngle;
	protected final List<Float> rndF;
	/**
	 * Current step. Works as counter
	 */
	protected int step = 0;
	/**
	 * Steps per iteration
	 */
	public int stepsPerIteration = 2;

	public DragonEffect(EffectManager effectManager) {
		super(effectManager);
		type = EffectType.REPEATING;
		period = 2;
		iterations = 200;
		rndF = new ArrayList<Float>(arcs);
		rndAngle = new ArrayList<Double>(arcs);
	}

	@Override
	public void onRun() {
		Location location = getLocation();
		for (int j = 0; j < stepsPerIteration; j++) {
			if (step % particles == 0) {
				rndF.clear();
				rndAngle.clear();
			}
			while (rndF.size() < arcs) {
				rndF.add(RandomUtils.random.nextFloat());
			}
			while (rndAngle.size() < arcs) {
				rndAngle.add(RandomUtils.getRandomAngle());
			}
			for (int i = 0; i < arcs; i++) {
				float pitch = rndF.get(i) * 2 * this.pitch - this.pitch;
				float x = (step % particles) * length / particles;
				float y = (float) (pitch * Math.pow(x, 2));
				Vector v = new Vector(x, y, 0);
				VectorUtils.rotateAroundAxisX(v, rndAngle.get(i));
				VectorUtils.rotateAroundAxisZ(v, -location.getPitch() * MathUtils.degreesToRadians);
				VectorUtils.rotateAroundAxisY(v, -(location.getYaw() + 90) * MathUtils.degreesToRadians);
				display(particle, location.add(v));
				location.subtract(v);
			}
			step++;
		}
	}
}

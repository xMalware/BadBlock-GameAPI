package fr.badblock.bukkit.hub.effectlib.effect;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.EffectManager;
import fr.badblock.bukkit.hub.effectlib.EffectType;
import fr.badblock.bukkit.hub.effectlib.util.ParticleEffect;
import fr.badblock.bukkit.hub.effectlib.util.RandomUtils;

public class TornadoEffect extends Effect {

	public Color cloudColor = null;
	/*
	 * Particle of the cloud
	 */
	public ParticleEffect cloudParticle = ParticleEffect.CLOUD;

	/*
	 * Size of the cloud
	 */
	public float cloudSize = 2.5f;
	/*
	 * Distance between each row
	 */
	public double distance = .375d;

	/*
	 * Max radius of the Tornado
	 */
	public float maxTornadoRadius = 5f;

	/*
	 * Should the cloud appear?
	 */
	public boolean showCloud = true;

	/*
	 * Should the tornado appear?
	 */
	public boolean showTornado = true;

	/*
	 * Internal counter
	 */
	protected int step = 0;

	public Color tornadoColor = null;

	/*
	 * Height of the Tornado
	 */
	public float tornadoHeight = 5f;

	/*
	 * Tornado particle
	 */
	public ParticleEffect tornadoParticle = ParticleEffect.FLAME;

	/*
	 * Y-Offset from location
	 */
	public double yOffset = .8;

	public TornadoEffect(EffectManager manager) {
		super(manager);
		type = EffectType.REPEATING;
		period = 5;
		iterations = 20;
	}

	public ArrayList<Vector> createCircle(double y, double radius) {
		double amount = radius * 64;
		double inc = (2 * Math.PI) / amount;
		ArrayList<Vector> vecs = new ArrayList<Vector>();
		for (int i = 0; i < amount; i++) {
			double angle = i * inc;
			double x = radius * Math.cos(angle);
			double z = radius * Math.sin(angle);
			Vector v = new Vector(x, y, z);
			vecs.add(v);
		}
		return vecs;
	}

	@Override
	public void onRun() {
		Location l = getLocation().add(0, yOffset, 0);
		for (int i = 0; i < (100 * cloudSize); i++) {
			Vector v = RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * cloudSize);
			if (showCloud) {
				display(cloudParticle, l.add(v), cloudColor, 0, 7);
				l.subtract(v);
			}
		}
		Location t = l.clone().add(0, .2, 0);
		double r = .45 * (maxTornadoRadius * (2.35 / tornadoHeight));
		for (double y = 0; y < tornadoHeight; y += distance) {
			double fr = r * y;
			if (fr > maxTornadoRadius) {
				fr = maxTornadoRadius;
			}
			for (Vector v : createCircle(y, fr)) {
				if (showTornado) {
					display(tornadoParticle, t.add(v), tornadoColor);
					t.subtract(v);
					step++;
				}
			}
		}
		l.subtract(0, yOffset, 0);
	}

}

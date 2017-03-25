package fr.badblock.bukkit.hub.effectlib.effect;

import org.bukkit.Location;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.EffectManager;
import fr.badblock.bukkit.hub.effectlib.EffectType;
import fr.badblock.bukkit.hub.effectlib.util.ParticleEffect;
import fr.badblock.bukkit.hub.effectlib.util.RandomUtils;

public class SmokeEffect extends Effect {

	/**
	 * ParticleType of spawned particle
	 */
	public ParticleEffect particle = ParticleEffect.SMOKE_NORMAL;

	public SmokeEffect(EffectManager effectManager) {
		super(effectManager);
		type = EffectType.REPEATING;
		period = 1;
		iterations = 300;
	}

	@Override
	public void onRun() {
		Location location = getLocation();
		for (int i = 0; i < 20; i++) {
			location.add(RandomUtils.getRandomCircleVector().multiply(RandomUtils.random.nextDouble() * 0.6d));
			location.add(0, RandomUtils.random.nextFloat() * 2, 0);
			display(particle, location);
		}
	}

}

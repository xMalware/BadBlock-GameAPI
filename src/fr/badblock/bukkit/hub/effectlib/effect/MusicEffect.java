package fr.badblock.bukkit.hub.effectlib.effect;

import org.bukkit.Location;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.EffectManager;
import fr.badblock.bukkit.hub.effectlib.EffectType;
import fr.badblock.bukkit.hub.effectlib.util.ParticleEffect;

public class MusicEffect extends Effect {

	/**
	 * Radials to spawn next note.
	 */
	public double radialsPerStep = Math.PI / 8;

	/**
	 * Radius of circle above head
	 */
	public float radius = .4f;

	/**
	 * Current step. Works as a counter
	 */
	protected float step = 0;

	public MusicEffect(EffectManager effectManager) {
		super(effectManager);
		type = EffectType.REPEATING;
		iterations = 400;
		period = 1;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRun() {
		Location location = getLocation();
		location.add(0, 1.9f, 0);
		location.add(Math.cos(radialsPerStep * step) * radius, 0, Math.sin(radialsPerStep * step) * radius);
		ParticleEffect.NOTE.display(location, visibleRange, 0, 0, 0, .5f, 1);
		step++;
	}

}

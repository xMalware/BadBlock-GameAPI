package fr.badblock.bukkit.hub.effectlib.effect;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import fr.badblock.bukkit.hub.effectlib.EffectManager;
import fr.badblock.bukkit.hub.effectlib.EffectType;
import fr.badblock.bukkit.hub.effectlib.util.RandomUtils;

public class BleedEffect extends fr.badblock.bukkit.hub.effectlib.Effect {

	/**
	 * Color of blood. Default is red (152)
	 */
	public int color = 152;

	/**
	 * Duration in ticks, the blood-particles take to despawn. Not used anymore
	 */
	@Deprecated
	public int duration = 10;

	/**
	 * Play the Hurt Effect for the Player
	 */
	public boolean hurt = true;

	public BleedEffect(EffectManager effectManager) {
		super(effectManager);
		type = EffectType.REPEATING;
		period = 4;
		iterations = 25;
	}

	@Override
	public void onRun() {
		// Location to spawn the blood-item.
		Location location = getLocation();
		location.add(0, RandomUtils.random.nextFloat() * 1.75f, 0);
		location.getWorld().playEffect(location, Effect.STEP_SOUND, color);

		Entity entity = getEntity();
		if (hurt && entity != null) {
			entity.playEffect(org.bukkit.EntityEffect.HURT);
		}
	}
}

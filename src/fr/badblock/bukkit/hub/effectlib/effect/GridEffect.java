package fr.badblock.bukkit.hub.effectlib.effect;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.EffectManager;
import fr.badblock.bukkit.hub.effectlib.EffectType;
import fr.badblock.bukkit.hub.effectlib.util.ParticleEffect;
import fr.badblock.bukkit.hub.effectlib.util.VectorUtils;

public class GridEffect extends Effect {

	/**
	 * Columns of the grid
	 */
	public int columns = 10;

	/**
	 * Height per cell in blocks
	 */
	public float heightCell = 1;

	/**
	 * ParticleType of the nucleus
	 */
	public ParticleEffect particle = ParticleEffect.FLAME;

	/**
	 * Particles to be spawned on the vertical borders of the cell
	 */
	public int particlesHeight = 3;

	/**
	 * Particles to be spawned on the horizontal borders of the cell
	 */
	public int particlesWidth = 4;

	/**
	 * Rotation around the Y-axis
	 */
	public double rotation = 0;

	/**
	 * Rows of the grid
	 */
	public int rows = 5;

	/**
	 * Width per cell in blocks
	 */
	public float widthCell = 1;

	public GridEffect(EffectManager effectManager) {
		super(effectManager);
		type = EffectType.INSTANT;
		period = 5;
		iterations = 50;
	}

	protected void addParticle(Location location, Vector v) {
		v.setZ(0);
		VectorUtils.rotateAroundAxisY(v, rotation);
		location.add(v);
		display(particle, location);
		location.subtract(v);
	}

	@Override
	public void onRun() {
		Location location = getLocation();
		// Draw rows
		Vector v = new Vector();
		for (int i = 0; i <= (rows + 1); i++) {
			for (int j = 0; j < particlesWidth * (columns + 1); j++) {
				v.setY(i * heightCell);
				v.setX(j * widthCell / particlesWidth);
				addParticle(location, v);
			}
		}
		// Draw columns
		for (int i = 0; i <= (columns + 1); i++) {
			for (int j = 0; j < particlesHeight * (rows + 1); j++) {
				v.setX(i * widthCell);
				v.setY(j * heightCell / particlesHeight);
				addParticle(location, v);
			}
		}
	}

}

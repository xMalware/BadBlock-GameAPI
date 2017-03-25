package fr.badblock.bukkit.hub.effectlib.effect;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.EffectManager;
import fr.badblock.bukkit.hub.effectlib.EffectType;
import fr.badblock.bukkit.hub.effectlib.util.MathUtils;
import fr.badblock.bukkit.hub.effectlib.util.ParticleEffect;
import fr.badblock.bukkit.hub.effectlib.util.StringParser;
import fr.badblock.bukkit.hub.effectlib.util.VectorUtils;

public class TextEffect extends Effect {

	/**
	 * Font to create the Text
	 */
	public Font font;

	/**
	 * Contains an image version of the String
	 */
	protected BufferedImage image = null;

	/**
	 * Invert the text
	 */
	public boolean invert = false;

	/**
	 * Particle to draw the text
	 */
	public ParticleEffect particle = ParticleEffect.FIREWORKS_SPARK;

	/**
	 * Set this only to true if you are working with changing text. I'll advice
	 * the parser to recalculate the BufferedImage every iteration. Recommended
	 * FALSE
	 */
	public boolean realtime = false;

	/**
	 * Scale the font down
	 */
	public float size = (float) 1 / 5;

	/**
	 * Each stepX pixel will be shown. Saves packets for lower fontsizes.
	 */
	public int stepX = 1;

	/**
	 * Each stepY pixel will be shown. Saves packets for lower fontsizes.
	 */
	public int stepY = 1;

	/**
	 * Text to display
	 */
	public String text = "Text";

	public TextEffect(EffectManager effectManager) {
		super(effectManager);
		this.font = new Font("Tahoma", Font.PLAIN, 16);
		type = EffectType.REPEATING;
		period = 40;
		iterations = 20;
	}

	@Override
	public void onRun() {
		if (font == null) {
			cancel();
			return;
		}
		Location location = getLocation();
		int clr = 0;
		try {
			if (image == null || realtime) {
				image = StringParser.stringToBufferedImage(font, text);
			}
			for (int y = 0; y < image.getHeight(); y += stepY) {
				for (int x = 0; x < image.getWidth(); x += stepX) {
					clr = image.getRGB(x, y);
					if (!invert && Color.black.getRGB() != clr) {
						continue;
					} else if (invert && Color.black.getRGB() == clr) {
						continue;
					}
					Vector v = new Vector((float) image.getWidth() / 2 - x, (float) image.getHeight() / 2 - y, 0)
							.multiply(size);
					VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);
					display(particle, location.add(v));
					location.subtract(v);
				}
			}
		} catch (Exception ex) {
			// This seems to happen on bad characters in strings,
			// I'm choosing to ignore the exception and cancel the effect for
			// now.
			cancel(true);
		}
	}

	public void setFont(Font font) {
		this.font = font;
	}
}

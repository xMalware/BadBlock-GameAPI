
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import java.io.IOException;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.ColoredImageEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class ColoredImageParticleItem extends ParticleItem {

	public ColoredImageParticleItem() {
		super("coloredimage");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return ColoredImageEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		ColoredImageEffect effect;
		try {
			effect = new ColoredImageEffect(getEffectManager());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}


package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.HelixEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class HelixParticleItem extends ParticleItem {

	public HelixParticleItem() {
		super("helix");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return HelixEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		HelixEffect effect = new HelixEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

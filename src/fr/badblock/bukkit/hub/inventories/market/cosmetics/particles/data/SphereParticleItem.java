
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.SphereEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class SphereParticleItem extends ParticleItem {

	public SphereParticleItem() {
		super("sphere");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return SphereEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		SphereEffect effect = new SphereEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

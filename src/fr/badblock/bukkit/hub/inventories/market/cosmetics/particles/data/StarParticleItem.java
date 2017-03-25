
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.StarEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class StarParticleItem extends ParticleItem {

	public StarParticleItem() {
		super("star");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return StarEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		StarEffect effect = new StarEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

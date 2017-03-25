
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.DonutEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class DonutParticleItem extends ParticleItem {

	public DonutParticleItem() {
		super("donut");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return DonutEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		DonutEffect effect = new DonutEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

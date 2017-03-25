
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.EquationEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class EquationParticleItem extends ParticleItem {

	public EquationParticleItem() {
		super("equation");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return EquationEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		EquationEffect effect = new EquationEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

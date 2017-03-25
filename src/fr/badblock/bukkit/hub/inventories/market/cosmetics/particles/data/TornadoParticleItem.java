
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.TornadoEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class TornadoParticleItem extends ParticleItem {

	public TornadoParticleItem() {
		super("tornado");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return TornadoEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		TornadoEffect effect = new TornadoEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

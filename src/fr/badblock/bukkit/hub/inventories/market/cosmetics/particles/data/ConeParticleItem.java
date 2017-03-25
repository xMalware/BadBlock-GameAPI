
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.ConeEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class ConeParticleItem extends ParticleItem {

	public ConeParticleItem() {
		super("cone");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return ConeEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		ConeEffect effect = new ConeEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

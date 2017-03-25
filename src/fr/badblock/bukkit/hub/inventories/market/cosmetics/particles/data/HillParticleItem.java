
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.HillEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class HillParticleItem extends ParticleItem {

	public HillParticleItem() {
		super("hill");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return HillEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		HillEffect effect = new HillEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

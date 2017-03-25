
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.HeartEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class HeartParticleItem extends ParticleItem {

	public HeartParticleItem() {
		super("heart");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return HeartEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		HeartEffect effect = new HeartEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

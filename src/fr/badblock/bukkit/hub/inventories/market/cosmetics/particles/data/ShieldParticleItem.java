
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.ShieldEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class ShieldParticleItem extends ParticleItem {

	public ShieldParticleItem() {
		super("shield");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return ShieldEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		ShieldEffect effect = new ShieldEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

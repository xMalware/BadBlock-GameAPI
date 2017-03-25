
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.FlameEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class FlameParticleItem extends ParticleItem {

	public FlameParticleItem() {
		super("flame");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return FlameEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		FlameEffect effect = new FlameEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

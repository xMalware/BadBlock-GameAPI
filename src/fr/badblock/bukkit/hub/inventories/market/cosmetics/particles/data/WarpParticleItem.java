
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.WarpEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class WarpParticleItem extends ParticleItem {

	public WarpParticleItem() {
		super("warp");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return WarpEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		WarpEffect effect = new WarpEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

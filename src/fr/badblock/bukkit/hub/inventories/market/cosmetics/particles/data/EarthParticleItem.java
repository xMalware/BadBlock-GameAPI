
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.EarthEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class EarthParticleItem extends ParticleItem {

	public EarthParticleItem() {
		super("earth");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return EarthEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		EarthEffect effect = new EarthEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

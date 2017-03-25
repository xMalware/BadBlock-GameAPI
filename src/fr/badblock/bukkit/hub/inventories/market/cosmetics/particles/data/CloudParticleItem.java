
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.CloudEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class CloudParticleItem extends ParticleItem {

	public CloudParticleItem() {
		super("cloud");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return CloudEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		CloudEffect effect = new CloudEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

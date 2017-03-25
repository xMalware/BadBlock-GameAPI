
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.CubeEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class CubeParticleItem extends ParticleItem {

	public CubeParticleItem() {
		super("cube");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return CubeEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		CubeEffect effect = new CubeEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

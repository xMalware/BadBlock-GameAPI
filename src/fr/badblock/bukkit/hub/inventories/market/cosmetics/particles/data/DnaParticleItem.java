
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.DnaEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class DnaParticleItem extends ParticleItem {

	public DnaParticleItem() {
		super("dna");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return DnaEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		DnaEffect effect = new DnaEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

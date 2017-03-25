
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.LineEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class LineParticleItem extends ParticleItem {

	public LineParticleItem() {
		super("line");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return LineEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		LineEffect effect = new LineEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

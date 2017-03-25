
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.DragonEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class DragonParticleItem extends ParticleItem {

	public DragonParticleItem() {
		super("dragon");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return DragonEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		DragonEffect effect = new DragonEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}


package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.JumpEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class JumpParticleItem extends ParticleItem {

	public JumpParticleItem() {
		super("jump");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return JumpEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		JumpEffect effect = new JumpEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

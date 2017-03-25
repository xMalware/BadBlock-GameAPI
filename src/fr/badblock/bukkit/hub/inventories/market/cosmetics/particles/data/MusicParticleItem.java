
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.MusicEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class MusicParticleItem extends ParticleItem {

	public MusicParticleItem() {
		super("music");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return MusicEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		MusicEffect effect = new MusicEffect(getEffectManager());
		effect.setEntity(player);
		effect.start();
		return effect;
	}

}

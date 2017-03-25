
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.data;

import org.bukkit.ChatColor;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.effectlib.effect.TextEffect;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults.ParticleItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class TextParticleItem extends ParticleItem {

	public TextParticleItem() {
		super("text");
	}

	@Override
	protected Class<? extends Effect> getEffectClass() {
		return TextEffect.class;
	}

	@Override
	protected Effect run(BadblockPlayer player, HubPlayer hubPlayer) {
		TextEffect effect = new TextEffect(getEffectManager());
		effect.setEntity(player);
		effect.text = ChatColor.RED + "BadBlock ^-^";
		effect.start();
		return effect;
	}

}

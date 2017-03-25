
package fr.badblock.bukkit.hub.inventories.market.cosmetics.particles.defaults;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.effectlib.Effect;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;

public class RemoveParticlesCosmeticsItem extends CustomItem {

	@SuppressWarnings("deprecation")
	public RemoveParticlesCosmeticsItem() {
		super("hub.items.removeparticlesitem", Material.getMaterial(397), (byte) 3,
				"hub.items.removeparticlesitem.lore");
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		HubPlayer hubPlayer = HubPlayer.get(player);
		if (hubPlayer.getParticles().isEmpty()) {
			particlesEmpty(player);
			return;
		}
		boolean contains = false;
		for (Effect effect : hubPlayer.getParticles())
			if (!effect.isDone())
				contains = true;
		if (!contains) {
			particlesEmpty(player);
			return;
		}
		hubPlayer.getParticles().forEach(particle -> particle.cancel());
		hubPlayer.getParticles().clear();
		player.sendTranslatedMessage("hub.items.removeparticlesitem.success");
	}

	public void particlesEmpty(BadblockPlayer player) {
		player.sendTranslatedMessage("hub.items.removeparticlesitem.youarenot");
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK,
				ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

}

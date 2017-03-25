
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class RabbitCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public RabbitCreatureMetamorphosisItem() {
		super("rabbit", Material.getMaterial(383), (byte) 101);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.RABBIT;
	}

}

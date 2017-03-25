
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class SilverfishCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public SilverfishCreatureMetamorphosisItem() {
		super("silverfish", Material.getMaterial(383), (byte) 60);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.SILVERFISH;
	}

}

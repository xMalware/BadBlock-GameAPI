
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class BlazeCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public BlazeCreatureMetamorphosisItem() {
		super("blaze", Material.getMaterial(383), (byte) 61);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.BLAZE;
	}

}

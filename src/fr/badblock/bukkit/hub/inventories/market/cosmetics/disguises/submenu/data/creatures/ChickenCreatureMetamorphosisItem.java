
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class ChickenCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public ChickenCreatureMetamorphosisItem() {
		super("chicken", Material.getMaterial(383), (byte) 93);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.CHICKEN;
	}

}

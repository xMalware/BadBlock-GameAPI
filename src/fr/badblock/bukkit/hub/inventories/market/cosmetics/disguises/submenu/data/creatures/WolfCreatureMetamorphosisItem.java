
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class WolfCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public WolfCreatureMetamorphosisItem() {
		super("wolf", Material.getMaterial(383), (byte) 95);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.WOLF;
	}

}

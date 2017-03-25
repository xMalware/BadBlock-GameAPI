
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class SkeletonCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public SkeletonCreatureMetamorphosisItem() {
		super("skeleton", Material.getMaterial(383), (byte) 51);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.SKELETON;
	}

}

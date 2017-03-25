
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class CaveSpiderCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public CaveSpiderCreatureMetamorphosisItem() {
		super("cavespider", Material.getMaterial(383), (byte) 59);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.CAVE_SPIDER;
	}

}

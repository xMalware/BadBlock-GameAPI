
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class PigCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public PigCreatureMetamorphosisItem() {
		super("pig", Material.getMaterial(383), (byte) 90);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.PIG;
	}

}

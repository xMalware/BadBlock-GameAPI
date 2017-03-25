
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class WitchCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public WitchCreatureMetamorphosisItem() {
		super("witch", Material.getMaterial(383), (byte) 66);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.WITCH;
	}

}

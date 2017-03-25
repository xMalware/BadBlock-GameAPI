
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class EndermanCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public EndermanCreatureMetamorphosisItem() {
		super("enderman", Material.getMaterial(383), (byte) 58);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.ENDERMAN;
	}

}

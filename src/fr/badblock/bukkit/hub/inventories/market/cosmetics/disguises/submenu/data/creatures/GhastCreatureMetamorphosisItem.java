
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class GhastCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public GhastCreatureMetamorphosisItem() {
		super("ghast", Material.getMaterial(383), (byte) 56);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.GHAST;
	}

}

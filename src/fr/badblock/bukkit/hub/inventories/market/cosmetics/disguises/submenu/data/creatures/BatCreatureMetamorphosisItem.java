
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class BatCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public BatCreatureMetamorphosisItem() {
		super("bat", Material.getMaterial(383), (byte) 65);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.BAT;
	}

}

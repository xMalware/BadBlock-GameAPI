
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class WitherSkullCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public WitherSkullCreatureMetamorphosisItem() {
		super("witherskull", Material.getMaterial(397), (byte) 1);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.WITHER_SKULL;
	}

}

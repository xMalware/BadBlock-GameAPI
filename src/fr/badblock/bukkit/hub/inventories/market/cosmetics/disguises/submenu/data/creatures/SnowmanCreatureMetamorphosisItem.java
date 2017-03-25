
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class SnowmanCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public SnowmanCreatureMetamorphosisItem() {
		super("snowman", Material.getMaterial(80), (byte) 0);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.SNOWMAN;
	}

}

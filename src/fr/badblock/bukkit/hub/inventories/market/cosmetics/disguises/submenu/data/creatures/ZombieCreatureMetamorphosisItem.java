
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class ZombieCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public ZombieCreatureMetamorphosisItem() {
		super("zombie", Material.getMaterial(383), (byte) 54);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.ZOMBIE;
	}

}

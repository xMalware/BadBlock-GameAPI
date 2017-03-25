
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class PigZombieCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public PigZombieCreatureMetamorphosisItem() {
		super("pigzombie", Material.getMaterial(383), (byte) 57);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.PIG_ZOMBIE;
	}

}

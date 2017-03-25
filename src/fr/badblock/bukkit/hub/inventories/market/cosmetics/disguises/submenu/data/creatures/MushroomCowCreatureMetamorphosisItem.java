
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class MushroomCowCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public MushroomCowCreatureMetamorphosisItem() {
		super("mushroomcow", Material.getMaterial(40), (byte) 0);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.MUSHROOM_COW;
	}

}

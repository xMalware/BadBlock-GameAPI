
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.creatures;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts.MetamorphosisCreatureItem;

public class IronGolemCreatureMetamorphosisItem extends MetamorphosisCreatureItem {

	@SuppressWarnings("deprecation")
	public IronGolemCreatureMetamorphosisItem() {
		super("irongolem", Material.getMaterial(267), (byte) 0);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return EntityType.IRON_GOLEM;
	}

}

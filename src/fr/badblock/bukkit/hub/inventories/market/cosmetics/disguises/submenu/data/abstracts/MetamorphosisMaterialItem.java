
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.submenu.data.abstracts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.parent.MetamorphosisItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetamorphosisMaterialItem extends MetamorphosisItem {

	public MetamorphosisMaterialItem(String metamorphosisName, Material material, byte data) {
		super(metamorphosisName, material, data);
	}

	@Override
	protected EntityType getMetamorphosisEntityType() {
		return null;
	}

}

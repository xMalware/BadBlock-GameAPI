
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.parent.items;

import org.bukkit.Material;

import fr.badblock.bukkit.hub.inventories.abstracts.items.simples.CustomInventoryOpenerItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.parent.inventories.submenu.MetamorphosisCreaturesInventory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetamorphosisCreatureSelectorItem extends CustomInventoryOpenerItem {

	@SuppressWarnings("deprecation")
	public MetamorphosisCreatureSelectorItem() {
		super(MetamorphosisCreaturesInventory.class, "MetamorphosisCreatureSelectorItem", Material.getMaterial(397), (byte) 2);
	}

}

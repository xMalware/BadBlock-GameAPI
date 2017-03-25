
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class MagmaCubeMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public MagmaCubeMountItem() {
		super("magmacube", Material.getMaterial(383), (byte) 62);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.MAGMA_CUBE;
	}

}

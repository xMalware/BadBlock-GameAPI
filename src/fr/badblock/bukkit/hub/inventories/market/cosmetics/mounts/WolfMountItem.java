
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class WolfMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public WolfMountItem() {
		super("wolf", Material.getMaterial(383), (byte) 95);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.WOLF;
	}

}

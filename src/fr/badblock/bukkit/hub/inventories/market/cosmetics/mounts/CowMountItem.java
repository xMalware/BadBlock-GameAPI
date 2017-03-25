
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class CowMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public CowMountItem() {
		super("cow", Material.getMaterial(383), (byte) 92);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.COW;
	}

}

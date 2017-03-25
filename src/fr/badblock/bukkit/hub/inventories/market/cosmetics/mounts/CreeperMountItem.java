
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class CreeperMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public CreeperMountItem() {
		super("creeper", Material.getMaterial(383), (byte) 50);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.CREEPER;
	}

}

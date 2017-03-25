
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class ZombieMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public ZombieMountItem() {
		super("zombie", Material.getMaterial(383), (byte) 54);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.ZOMBIE;
	}

}

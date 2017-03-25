
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class BunnyMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public BunnyMountItem() {
		super("bunny", Material.getMaterial(383), (byte) 101);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.RABBIT;
	}

}


package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class SquidMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public SquidMountItem() {
		super("squid", Material.getMaterial(383), (byte) 94);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.SQUID;
	}

}

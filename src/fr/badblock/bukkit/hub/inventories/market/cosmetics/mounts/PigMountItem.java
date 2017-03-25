
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class PigMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public PigMountItem() {
		super("pig", Material.getMaterial(383), (byte) 90);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.PIG;
	}

}

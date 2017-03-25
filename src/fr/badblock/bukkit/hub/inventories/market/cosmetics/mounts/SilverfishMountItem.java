
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class SilverfishMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public SilverfishMountItem() {
		super("silverfish", Material.getMaterial(383), (byte) 60);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.SILVERFISH;
	}

}

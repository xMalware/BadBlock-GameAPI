
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class CaveSpiderMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public CaveSpiderMountItem() {
		super("cavespider", Material.getMaterial(383), (byte) 59);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.CAVE_SPIDER;
	}

}

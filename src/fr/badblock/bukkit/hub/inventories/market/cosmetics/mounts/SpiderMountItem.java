
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class SpiderMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public SpiderMountItem() {
		super("spider", Material.getMaterial(383), (byte) 52);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.SPIDER;
	}

}

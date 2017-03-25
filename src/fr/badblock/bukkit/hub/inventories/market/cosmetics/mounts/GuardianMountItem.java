
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class GuardianMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public GuardianMountItem() {
		super("guardian", Material.getMaterial(383), (byte) 68);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.GUARDIAN;
	}

}


package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class BatMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public BatMountItem() {
		super("bat", Material.getMaterial(383), (byte) 65);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.BAT;
	}

}

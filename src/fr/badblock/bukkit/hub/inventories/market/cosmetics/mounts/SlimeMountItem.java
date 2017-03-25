
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class SlimeMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public SlimeMountItem() {
		super("slime", Material.getMaterial(383), (byte) 55);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.SLIME;
	}

}

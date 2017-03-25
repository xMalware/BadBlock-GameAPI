
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class WitchMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public WitchMountItem() {
		super("witch", Material.getMaterial(383), (byte) 66);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.WITCH;
	}

}

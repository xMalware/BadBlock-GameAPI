
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class PegasusMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public PegasusMountItem() {
		super("pegasus", Material.getMaterial(383), (byte) 100);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.HORSE;
	}

}

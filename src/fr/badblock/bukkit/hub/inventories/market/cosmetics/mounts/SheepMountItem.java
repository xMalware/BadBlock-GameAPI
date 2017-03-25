
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class SheepMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public SheepMountItem() {
		super("sheep", Material.getMaterial(383), (byte) 91);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.SHEEP;
	}

}

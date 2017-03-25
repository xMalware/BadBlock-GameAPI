
package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.defaults.MountItem;

public class OcelotMountItem extends MountItem {

	@SuppressWarnings("deprecation")
	public OcelotMountItem() {
		super("ocelot", Material.getMaterial(383), (byte) 98);
	}

	@Override
	protected EntityType getMountEntityType() {
		return EntityType.OCELOT;
	}

}

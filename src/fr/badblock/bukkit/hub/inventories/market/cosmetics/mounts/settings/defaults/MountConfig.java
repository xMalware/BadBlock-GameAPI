package fr.badblock.bukkit.hub.inventories.market.cosmetics.mounts.settings.defaults;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MountConfig {

	private boolean baby;
	private boolean funMode;
	private boolean glint;
	private String mountName;
	private boolean pegasus;
	private boolean reverse;

	public MountConfig(String mountName) {
		this.setMountName(mountName);
	}

}

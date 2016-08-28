package fr.badblock.game.core18R3.defaultinventories;

import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;

public abstract class DefaultInventory {
	public abstract ItemStackExtra create(String key);

	public abstract String getKey();

	public abstract DefaultInventory getPrevious();
}

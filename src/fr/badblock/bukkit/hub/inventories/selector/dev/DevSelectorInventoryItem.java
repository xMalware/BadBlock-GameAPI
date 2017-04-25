package fr.badblock.bukkit.hub.inventories.selector.dev;

import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.rabbitmq.DevAliveFactory;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;

public class DevSelectorInventoryItem extends CustomItem {
	private String name, displayname;
	private int players, slot;	
	
	public DevSelectorInventoryItem(DevAliveFactory daf)
	{
		super("", Material.WOOL);
		apply(daf);
	}
	
	public boolean canApply(DevAliveFactory factory)
	{
		return name.equalsIgnoreCase(factory.name);
	}
	
	public boolean apply(DevAliveFactory daf)
	{
		this.name = daf.name;
		this.players = daf.players;
		this.slot = daf.slots;

		staticItem.clear();
		
		return !daf.open;
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction action, Block clickedBlock) {
		player.closeInventory();
		player.sendPlayer(name);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemStack toItemStack(Locale locale)
	{
		if (locale == null)
			return null;

		ItemStack itemStack = new ItemStack(Material.WOOL, 1, players >= slot ? DyeColor.RED.getData() : DyeColor.GREEN.getData());
		return ItemStackUtils.changeDisplay(itemStack, displayname + " (" + players + "/" + slot + ")");
	}
}

package fr.badblock.bukkit.hub.inventories.selector.dev;

import java.util.Arrays;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import fr.badblock.bukkit.hub.BadBlockHub;
import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.rabbitmq.factories.DevAliveFactory;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.i18n.Locale;
import fr.badblock.gameapi.utils.itemstack.ItemStackUtils;
import fr.badblock.rabbitconnector.RabbitPacketType;
import fr.badblock.utils.Encodage;

public class DevSelectorInventoryItem extends CustomItem {
	private String name;
	private int players, slot;	
	private boolean openStaff;
	
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
		this.openStaff = daf.openStaff;

		staticItem.clear();
		
		for(Locale loc : Locale.values())
			staticItem.put(loc, toItemStack(loc));
		
		return !daf.open;
	}

	@Override
	public List<ItemAction> getActions() {
		return Arrays.asList(ItemAction.INVENTORY_DROP, ItemAction.INVENTORY_LEFT_CLICK, ItemAction.INVENTORY_RIGHT_CLICK, ItemAction.INVENTORY_WHEEL_CLICK);
	}

	@Override
	public void onClick(BadblockPlayer player, ItemAction action, Block clickedBlock) {
		player.closeInventory();
		
		if(openStaff && !player.hasPermission("devserver"))
			player.sendTranslatedMessage("hub.items.devinventory.notforstaff");
		else if(action == ItemAction.INVENTORY_WHEEL_CLICK)
		{
			BadBlockHub.getInstance().getRabbitService().sendAsyncPacket("forcekill", name, Encodage.UTF8, RabbitPacketType.PUBLISHER, 5000, false);
			player.sendMessage("§aKill message send..");
		}
		else
		{
			player.sendMessage("§aLet's go!");
			player.sendPlayer(name);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ItemStack toItemStack(Locale locale)
	{
		if (locale == null)
			return null;

		ItemStack itemStack = new ItemStack(Material.WOOL, 1, players >= slot ? DyeColor.RED.getData() : (openStaff ? DyeColor.GREEN.getData() : DyeColor.ORANGE.getData()));
		return ItemStackUtils.changeDisplay(itemStack, name + " (" + players + "/" + slot + ")");
	}
}

package fr.badblock.game.core18R3.listeners;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.BadListener;

public class PickupItemListener extends BadListener {
	
	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent e){
		setMaxStackSize(e.getItem().getItemStack(), 256);
		e.getItem().getItemStack().setAmount(256);
	}

	public static ItemStack setMaxStackSize(ItemStack is, int amount){
		try {
			net.minecraft.server.v1_8_R3.ItemStack nmsIS = CraftItemStack.asNMSCopy(is);
			nmsIS.getItem().c(amount);
			return CraftItemStack.asBukkitCopy(nmsIS);
		} catch (Throwable t) { }

		return null;
	}


}

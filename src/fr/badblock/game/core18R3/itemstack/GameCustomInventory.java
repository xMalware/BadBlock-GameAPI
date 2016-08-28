package fr.badblock.game.core18R3.itemstack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.utils.itemstack.CustomInventory;
import fr.badblock.gameapi.utils.itemstack.ItemAction;
import fr.badblock.gameapi.utils.itemstack.ItemEvent;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra;
import fr.badblock.gameapi.utils.itemstack.ItemStackExtra.ItemPlaces;

public class GameCustomInventory implements CustomInventory {
	private int lines;
	private Inventory handler;

	public GameCustomInventory(String displayName, int lines) {
		this.lines = lines;
		this.handler = Bukkit.createInventory(null, lines * 9, displayName);
	}

	@Override
	public CustomInventory addClickableItem(int slot, ItemStack item, ItemEvent event) {
		if (item == null)
			removeItem(slot);
		else {
			addClickableItem(slot, new GameItemExtra(item).listenAs(event, ItemPlaces.INVENTORY_CLICKABLE));
		}

		return this;
	}

	@Override
	public CustomInventory addClickableItem(int slot, ItemStackExtra item) {
		if (item == null)
			removeItem(slot);
		else
			setItem(slot, item.getHandler());

		return this;
	}

	@Override
	public CustomInventory addDecorationItem(int slot, ItemStack item) {
		addClickableItem(slot, item, new ItemEvent() {
			@Override
			public boolean call(ItemAction action, BadblockPlayer player) {
				return true;
			}
		});
		return this;
	}

	@Override
	public int getLineCount() {
		return lines;
	}

	@Override
	public void openInventory(BadblockPlayer player) {
		player.openInventory(handler);
	}

	@Override
	public CustomInventory removeItem(int slot) {
		setItem(slot, new ItemStack(Material.AIR, 1));
		return this;
	}

	private void setItem(int slot, ItemStack item) {
		handler.setItem(slot, item);
	}

	@Override
	public int size() {
		return getLineCount() * 9;
	}
}

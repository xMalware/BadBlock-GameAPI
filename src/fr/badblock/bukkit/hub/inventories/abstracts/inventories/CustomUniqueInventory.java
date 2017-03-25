package fr.badblock.bukkit.hub.inventories.abstracts.inventories;

import java.util.Map.Entry;

import org.bukkit.Bukkit;

import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CustomUniqueInventory extends CustomInventory {

	public CustomUniqueInventory(String name) {
		this(name, 6);
	}

	public CustomUniqueInventory(String name, int lines) {
		super(name, lines);
	}

	@Override
	public void open(BadblockPlayer player) {
		player.closeInventory();
		HubPlayer.get(player).setCurrentInventory(this);
		if (inventory == null) {
			inventory = Bukkit.createInventory(null, this.getLines() * 9, this.getName());
			for (Entry<Integer, CustomItem> entry : items.entrySet())
				inventory.setItem(entry.getKey(), entry.getValue().toItemStack(player));
		}
		player.openInventory(inventory);
	}

}

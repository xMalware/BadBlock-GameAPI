package fr.badblock.bukkit.hub.inventories.selector.dev;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.abstracts.items.CustomItem;
import fr.badblock.bukkit.hub.objects.HubPlayer;
import fr.badblock.bukkit.hub.rabbitmq.factories.DevAliveFactory;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayer;

public class DevSelectorInventory extends CustomInventory {
	private static List<DevSelectorInventoryItem> servers = new ArrayList<>();

	public static final DevSelectorInventory inventory = new DevSelectorInventory();
	
	public static void Apply(DevAliveFactory daf)
	{
		for(int i = 0; i < servers.size(); i++)
		{
			DevSelectorInventoryItem item = servers.get(i);
			
			if(item.canApply(daf))
			{
				if(item.apply(daf))
				{
					servers.remove(i);
					inventory.create();
				}
				else inventory.update(i);
				return;
			}
		}
		
		servers.add( new DevSelectorInventoryItem(daf) );
		inventory.create();
	}
	
	public DevSelectorInventory() {
		super("hub.items.devinventory.displayname", 6);
	}
	
	protected void create()
	{
		items.clear();
		
		for(int i = 0; i < servers.size(); i++)
			setItem(i, servers.get(i));
		
		for (BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers()) {
			HubPlayer hubPlayer = HubPlayer.get(player);
			
			if (hubPlayer.getCurrentInventory() == null || player.getOpenInventory() == null)
				continue;
			if (hubPlayer.getCurrentInventory() != this)
				continue;
			
			open(player);
		}
	}
	
	protected void update(int slot)
	{
		CustomItem item = getItems().get(slot);
		
		if(item == null)
			return;
		
		for (BadblockPlayer player : GameAPI.getAPI().getOnlinePlayers()) {
			HubPlayer hubPlayer = HubPlayer.get(player);
			
			if (hubPlayer.getCurrentInventory() == null || player.getOpenInventory() == null)
				continue;
			if (hubPlayer.getCurrentInventory() != this)
				continue;
			
			getKeysByValue(hubPlayer.getCurrentInventory().getItems(), item).stream().forEach(
						s -> player.getOpenInventory().getTopInventory().setItem(s, item.toItemStack(player)));
		}

		items.get(slot);		
	}
	
	private static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
		return map.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), value)).map(Map.Entry::getKey)
				.collect(Collectors.toSet());
	}
}

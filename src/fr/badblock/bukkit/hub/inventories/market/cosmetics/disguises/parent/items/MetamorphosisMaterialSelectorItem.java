
package fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.parent.items;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.badblock.bukkit.hub.inventories.abstracts.actions.ItemAction;
import fr.badblock.bukkit.hub.inventories.abstracts.inventories.CustomInventory;
import fr.badblock.bukkit.hub.inventories.abstracts.items.simples.CustomInventoryOpenerItem;
import fr.badblock.bukkit.hub.inventories.market.cosmetics.disguises.parent.inventories.submenu.MetamorphosisMaterialsInventory;
import fr.badblock.gameapi.players.BadblockPlayer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetamorphosisMaterialSelectorItem extends CustomInventoryOpenerItem {

	public MetamorphosisMaterialSelectorItem() {
		super(MetamorphosisMaterialsInventory.class, "MetamorphosisMaterialSelectorItem", Material.QUARTZ_BLOCK);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(BadblockPlayer player, ItemAction itemAction, Block clickedBlock) {
		if (!player.hasPermission("hub.soonbypass")) {
			player.sendTranslatedMessage("hub.items.functionsoon");
			return;
		}
		CustomInventory.get(this.getClazz()).open(player);
	}

}

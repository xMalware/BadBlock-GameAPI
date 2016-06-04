package fr.badblock.game.v1_8_R3.listeners.packets;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.badblock.gameapi.packets.OutPacketListener;
import fr.badblock.gameapi.packets.out.play.PlayEntityEquipment;
import fr.badblock.gameapi.players.BadblockPlayer;

public class EquipmentListener extends OutPacketListener<PlayEntityEquipment> {
	@Override
	public void listen(BadblockPlayer player, PlayEntityEquipment packet) {
		BadblockPlayer p = null;
		
		for(Player e : Bukkit.getOnlinePlayers()) {
			if(e.getEntityId() == packet.getEntityId()) {
				p = (BadblockPlayer) e;
				break;
			}
		}
		
		if(p != null && p.isDisguised()){
			packet.setItemStack(new ItemStack(Material.AIR, 1));
		}
	}
}

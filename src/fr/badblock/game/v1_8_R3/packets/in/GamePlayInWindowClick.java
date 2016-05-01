package fr.badblock.game.v1_8_R3.packets.in;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.badblock.game.v1_8_R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInWindowClick;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;

@Getter public class GamePlayInWindowClick extends GameBadblockInPacket implements PlayInWindowClick {
	private int 	  windowId;
	private int 	  slot;
	private int 	  actionNumber;
	private int 	  button;
	private int 	  mode;
	private ItemStack item;
	
	public GamePlayInWindowClick(PacketPlayInWindowClick packet){
		super(packet);
		this.windowId 	  = packet.a();
		this.slot     	  = packet.b();
		this.button	      = packet.c();
		this.actionNumber = packet.d();
		this.mode		  = packet.f();
		this.item		  = CraftItemStack.asBukkitCopy(packet.e());
	}
}

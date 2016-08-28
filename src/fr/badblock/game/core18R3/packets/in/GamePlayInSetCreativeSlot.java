package fr.badblock.game.core18R3.packets.in;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInSetCreativeSlot;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInSetCreativeSlot;

@Getter
public class GamePlayInSetCreativeSlot extends GameBadblockInPacket implements PlayInSetCreativeSlot {
	private int slot;
	private ItemStack itemStack;

	public GamePlayInSetCreativeSlot(PacketPlayInSetCreativeSlot packet) {
		super(packet);
		slot = packet.a();
		itemStack = CraftItemStack.asBukkitCopy(packet.getItemStack());
	}
}

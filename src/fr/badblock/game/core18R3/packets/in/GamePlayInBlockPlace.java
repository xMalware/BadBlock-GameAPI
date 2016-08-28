package fr.badblock.game.core18R3.packets.in;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.badblock.game.core18R3.packets.GameBadblockInPacket;
import fr.badblock.gameapi.packets.in.play.PlayInBlockDig.BlockFace;
import fr.badblock.gameapi.packets.in.play.PlayInBlockPlace;
import fr.badblock.gameapi.utils.selections.Vector3f;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;

@Getter
public class GamePlayInBlockPlace extends GameBadblockInPacket implements PlayInBlockPlace {
	private Vector3f blockPosition;
	private BlockFace blockFace;
	private ItemStack itemStack;
	private Vector3f cursorPosition;

	public GamePlayInBlockPlace(PacketPlayInBlockPlace packet) {
		super(packet);
		this.blockPosition = new Vector3f(packet.a().getX(), packet.a().getY(), packet.a().getZ());
		this.blockFace = BlockFace.getById(packet.getFace());
		this.itemStack = CraftItemStack.asBukkitCopy(packet.getItemStack());
		this.cursorPosition = new Vector3f(packet.d(), packet.e(), packet.f());
	}
}

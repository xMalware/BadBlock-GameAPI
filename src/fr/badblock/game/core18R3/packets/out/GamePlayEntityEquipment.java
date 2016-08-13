package fr.badblock.game.core18R3.packets.out;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayEntityEquipment;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayEntityEquipment extends GameBadblockOutPacket implements PlayEntityEquipment {
	private int 		  entityId  = 0;
	private int			  slot      = 0;
	private ItemStack	  itemStack = null;
	
	public GamePlayEntityEquipment	(PacketPlayOutEntityEquipment packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId  = (int) reflector.getFieldValue("a");
			slot 	  = (int) reflector.getFieldValue("b");
			itemStack = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_8_R3.ItemStack) reflector.getFieldValue("c"));
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		return new PacketPlayOutEntityEquipment(entityId, slot, CraftItemStack.asNMSCopy(itemStack));
	}
}

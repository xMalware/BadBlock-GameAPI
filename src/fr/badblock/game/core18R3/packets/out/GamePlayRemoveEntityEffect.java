package fr.badblock.game.core18R3.packets.out;

import org.bukkit.potion.PotionEffectType;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayRemoveEntityEffect;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutRemoveEntityEffect;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayRemoveEntityEffect extends GameBadblockOutPacket implements PlayRemoveEntityEffect {
	private int 			 entityId = 0;
	private PotionEffectType effect;
	
	@SuppressWarnings("deprecation")
	public GamePlayRemoveEntityEffect(PacketPlayOutRemoveEntityEffect packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			entityId = (int) reflector.getFieldValue("a");
			effect   = PotionEffectType.getById((int) reflector.getFieldValue("b"));
		} catch(Exception e){}
	}
	
	@SuppressWarnings("deprecation") @Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutRemoveEntityEffect packet = new PacketPlayOutRemoveEntityEffect();
		Reflector reflector = new Reflector(packet);
		
		reflector.setFieldValue("a", entityId);
		reflector.setFieldValue("b", effect.getId());
		
		return packet;
	}
}

package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayChangeGameState;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_8_R3.PacketPlayOutGameStateChange;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayChangeGameState extends GameBadblockOutPacket implements PlayChangeGameState {
	private GameState state = GameState.ENTER_CREDITS;
	private float	  value = 0f;
	
	public GamePlayChangeGameState(PacketPlayOutCamera packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			state = GameState.getFromId((int) reflector.getFieldValue("b"));
			value = (float) reflector.getFieldValue("c");

		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutGameStateChange packet   = new PacketPlayOutGameStateChange();
		Reflector					 reflector = new Reflector(packet);
		
		reflector.setFieldValue("b", state.getValue());
		reflector.setFieldValue("c", value);
		
		return packet;
	}

}

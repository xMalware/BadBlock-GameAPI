package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardDisplayObjective;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayScoreboardDisplayObjective extends GameBadblockOutPacket implements PlayScoreboardDisplayObjective {
	private ObjectivePosition objectivePosition;
	private String			  objectiveName;

	public GamePlayScoreboardDisplayObjective(PacketPlayOutScoreboardDisplayObjective packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			objectivePosition = ObjectivePosition.getByValue((byte) reflector.getFieldValue("a"));
			objectiveName     = (String) reflector.getFieldValue("b");
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutScoreboardDisplayObjective objective = new PacketPlayOutScoreboardDisplayObjective();
		
		Reflector reflector = new Reflector(objective);
		reflector.setFieldValue("b", objectiveName);
		reflector.setFieldValue("a", (int) objectivePosition.getData());
		
		return objective;
	}
}

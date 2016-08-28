package fr.badblock.game.core18R3.packets.out;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardObjective;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria.EnumScoreboardHealthDisplay;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayScoreboardObjective extends GameBadblockOutPacket implements PlayScoreboardObjective {
	private String objectiveName = "";
	private ObjectiveMode mode = ObjectiveMode.CREATE;
	private String displayName = "";
	private ObjectiveType objectiveType = ObjectiveType.INTEGER;

	public GamePlayScoreboardObjective(PacketPlayOutScoreboardObjective packet) {
		Reflector reflector = new Reflector(packet);

		try {
			mode = ObjectiveMode.getByValue((byte) reflector.getFieldValue("d"));
			objectiveName = (String) reflector.getFieldValue("a");

			if (mode != ObjectiveMode.REMOVE) {
				displayName = (String) reflector.getFieldValue("b");
				objectiveType = ObjectiveType
						.valueOf(((EnumScoreboardHealthDisplay) reflector.getFieldValue("c")).name());
			}
		} catch (Exception e) {
		}
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutScoreboardObjective objective = new PacketPlayOutScoreboardObjective();

		Reflector reflector = new Reflector(objective);
		reflector.setFieldValue("a", objectiveName);
		reflector.setFieldValue("d", (int) mode.getData());

		if (mode != ObjectiveMode.REMOVE) {
			reflector.setFieldValue("b", displayName);
			reflector.setFieldValue("c", EnumScoreboardHealthDisplay.valueOf(objectiveType.name()));
		}

		return objective;
	}
}

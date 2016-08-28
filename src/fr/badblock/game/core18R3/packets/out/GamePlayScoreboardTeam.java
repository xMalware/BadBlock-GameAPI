package fr.badblock.game.core18R3.packets.out;

import java.util.Arrays;
import java.util.List;

import fr.badblock.game.core18R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardTeam;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true, fluent = false)
public class GamePlayScoreboardTeam extends GameBadblockOutPacket implements PlayScoreboardTeam {
	private String teamName = "";
	private TeamMode mode = TeamMode.CREATE;
	private String displayName = "";
	private String prefix = "";
	private String suffix = "";
	private TeamFriendlyFire friendlyFire = TeamFriendlyFire.OFF;
	private TeamNameTag nametagVisibility = TeamNameTag.always;
	private byte color = -1;
	private String[] players = new String[] {};

	public GamePlayScoreboardTeam(PacketPlayOutScoreboardTeam packet) {
		Reflector reflector = new Reflector(packet);

		try {
			teamName = (String) reflector.getFieldValue("a");
			mode = TeamMode.getByValue((byte) reflector.getFieldValue("h"));

			if (mode == TeamMode.CREATE || mode == TeamMode.UPDATE) {
				displayName = (String) reflector.getFieldValue("b");
				prefix = (String) reflector.getFieldValue("c");
				suffix = (String) reflector.getFieldValue("d");

				friendlyFire = TeamFriendlyFire.getByValue((byte) reflector.getFieldValue("i"));
				nametagVisibility = TeamNameTag.valueOf((String) reflector.getFieldValue("e"));

				color = (byte) reflector.getFieldValue("f");
			}

			if (mode == TeamMode.CREATE || mode == TeamMode.ADD_PLAYERS || mode == TeamMode.REMOVE_PLAYERS) {
				players = ((List<?>) reflector.getFieldValue("g")).toArray(new String[0]);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutScoreboardTeam objective = new PacketPlayOutScoreboardTeam();

		Reflector reflector = new Reflector(objective);

		reflector.setFieldValue("a", teamName);
		reflector.setFieldValue("h", (int) mode.getData());

		if (mode == TeamMode.CREATE || mode == TeamMode.UPDATE) {
			reflector.setFieldValue("b", displayName);
			reflector.setFieldValue("c", prefix);
			reflector.setFieldValue("d", suffix);
			reflector.setFieldValue("i", friendlyFire.getData());
			reflector.setFieldValue("e", nametagVisibility.name());
			reflector.setFieldValue("f", (int) color);
		}

		if (mode == TeamMode.CREATE || mode == TeamMode.ADD_PLAYERS || mode == TeamMode.REMOVE_PLAYERS) {
			reflector.setFieldValue("g", Arrays.asList(players));
		}

		return objective;
	}
}

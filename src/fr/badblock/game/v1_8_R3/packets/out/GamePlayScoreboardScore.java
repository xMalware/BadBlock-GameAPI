package fr.badblock.game.v1_8_R3.packets.out;

import fr.badblock.game.v1_8_R3.packets.GameBadblockOutPacket;
import fr.badblock.gameapi.packets.out.play.PlayScoreboardScore;
import fr.badblock.gameapi.utils.reflection.Reflector;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria.EnumScoreboardHealthDisplay;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore.EnumScoreboardAction;

@NoArgsConstructor@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true, fluent = false)
public class GamePlayScoreboardScore extends GameBadblockOutPacket implements PlayScoreboardScore {
	private String    scoreName = "";
	private ScoreMode mode = ScoreMode.CHANGE;
	private String    objectiveName = "";
	private int 	  score = 0;

	public GamePlayScoreboardScore(PacketPlayOutScoreboardScore packet){
		Reflector reflector = new Reflector(packet);
		
		try {
			scoreName 		  = (String) reflector.getFieldValue("a");
			objectiveName     = (String) reflector.getFieldValue("b");
			
			mode			  = ScoreMode.valueOf(((EnumScoreboardHealthDisplay) reflector.getFieldValue("d")).name());
			
			if(mode == ScoreMode.CHANGE){
				score = (int) reflector.getFieldValue("c");
			}
		} catch(Exception e){}
	}
	
	@Override
	public Packet<?> buildPacket() throws Exception {
		PacketPlayOutScoreboardScore score = new PacketPlayOutScoreboardScore();
		
		Reflector reflector = new Reflector(score);
		reflector.setFieldValue("a", scoreName);
		reflector.setFieldValue("b", objectiveName);
		
		if(mode == ScoreMode.CHANGE){
			reflector.setFieldValue("c", this.score);
		}
		
		reflector.setFieldValue("d", EnumScoreboardAction.valueOf(mode.name()));
		
		return score;	
	}
}

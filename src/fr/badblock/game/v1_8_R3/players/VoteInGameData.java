package fr.badblock.game.v1_8_R3.players;

import fr.badblock.gameapi.players.BadblockScoreboard.VoteElement;
import fr.badblock.gameapi.players.data.InGameData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class VoteInGameData implements InGameData {
	@Getter@Setter private VoteElement element;
}

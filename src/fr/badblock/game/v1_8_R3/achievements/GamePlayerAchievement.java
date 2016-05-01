package fr.badblock.game.v1_8_R3.achievements;

import fr.badblock.gameapi.players.PlayerAchievement;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import lombok.Getter;

public class GamePlayerAchievement implements PlayerAchievement {
	@Getter
	private String achievementName;
	private String displayName;
	private String description;
	@Getter
	private boolean onegameAchievement;
	@Getter
	private int achievementNeededValue;
	@Getter
	private int megaCoinsReward;
	@Getter
	private int xpReward;
	@Getter
	private int badcoinsReward;
	
	@Override
	public TranslatableString getDisplayName() {
		return new TranslatableString(displayName);
	}
	@Override
	public TranslatableString getDescription() {
		return new TranslatableString(description);
	}
}

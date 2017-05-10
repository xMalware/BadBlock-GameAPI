package fr.badblock.game.core18R3.minigame;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.achievements.AchievementList;
import fr.badblock.gameapi.minigame.BadBlockGamePlugin;
import fr.badblock.gameapi.run.BadblockGameData;
import fr.badblock.gameapi.run.RunType;

public abstract class GameBadBlockGamePlugin extends BadBlockGamePlugin {

	@Override
	public void onGameLoad() {
		// Load achievements before everything
		loadAchievements();
		
		// Exclude game load if it's a lobby
		if (GameAPI.getAPI().getRunType().equals(RunType.LOBBY)) return;
		
		// Create plugin folder if it doesn't exist
		if (!getDataFolder().exists()) getDataFolder().mkdir();
		
		// Init the server as a game instance of this game
		this.getGame().use();
	}

	@Override
	public void loadAchievements() {
		// If there's not achievement list, we don't load them.
		if (getAchievementList() == null) return;
		// Load achievements
		getGame().setGameData(new BadblockGameData() {
			@Override
			public AchievementList getAchievements() {
				return getAchievementList();
			}
		});
	}
	
}

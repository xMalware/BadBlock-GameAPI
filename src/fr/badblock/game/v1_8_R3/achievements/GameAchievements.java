package fr.badblock.game.v1_8_R3.achievements;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.badblock.gameapi.players.PlayerAchievement;
import fr.badblock.gameapi.utils.general.JsonUtils;

public class GameAchievements {
	private Map<String, Map<String, PlayerAchievement>> achievements;
	
	public GameAchievements(File main) {
		main.mkdirs();
		
		achievements = Maps.newConcurrentMap();
		
		for(File folder : main.listFiles()){
			if(!folder.isDirectory())
				continue;
			
			String game = folder.getName().toLowerCase();
			Map<String, PlayerAchievement> achievements = Maps.newConcurrentMap();
			
			for(File file : folder.listFiles()){
				if(file.isDirectory())
					continue;
				
				GamePlayerAchievement achievement = JsonUtils.load(file, GamePlayerAchievement.class);
				
				if(achievement == null) continue;
				JsonUtils.save(file, achievement, true);
				
				achievements.put(achievement.getAchievementName().toLowerCase(), achievement);
			}
			
			this.achievements.put(game, achievements);
		}
	}
	
	public Collection<String> getGames(){
		return achievements.keySet();
	}
	
	public Collection<PlayerAchievement> getAchievements(String game){
		game = game.toLowerCase();
		
		if(achievements.containsKey(game)){
			return achievements.get(game).values();
		} else return Lists.newArrayList();
	}
	
	public PlayerAchievement getAchievement(String game){
		game = game.toLowerCase();
		
		for(Map<String, PlayerAchievement> list : achievements.values())
			if(list.containsKey(game))
				return list.get(game);
		return null;
	}
}

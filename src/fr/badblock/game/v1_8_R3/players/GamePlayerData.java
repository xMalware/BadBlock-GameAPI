package fr.badblock.game.v1_8_R3.players;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import fr.badblock.gameapi.players.PlayerAchievement;
import fr.badblock.gameapi.players.data.PlayerAchievementState;
import fr.badblock.gameapi.players.data.PlayerData;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.utils.i18n.Locale;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter@ToString
public class GamePlayerData implements PlayerData {
	private int  				 				badcoins     = 0;
	private int  				 				level	     = 1;
	private long 								xp		     = 0L;
	private int  				 				coinsBonus   = 0;
	private int  				 				xpBonus  	 = 0;
	private Map<String, Integer> 				kits 		 = Maps.newConcurrentMap();
	private Map<String, String>					lastUsedKits = Maps.newConcurrentMap();
	private Map<String, PlayerAchievementState> achievements = Maps.newConcurrentMap();
	private Map<String, Map<String, Double>> 	stats   	 = Maps.newConcurrentMap();
	
	private transient Map<String, PlayerData> 	datas 		 = Maps.newConcurrentMap();
	private transient JsonObject 				data		 = new JsonObject();
	
	@Override
	public void addBadcoins(int badcoins) {
		this.badcoins += Math.abs(badcoins);
	}

	@Override
	public void removeBadcoins(int badcoins) {
		this.badcoins -= Math.abs(badcoins);
	}

	@Override
	public long getXpUntilNextLevel() {
		return (int)(Math.pow(1.1d, level + 1) * 100);
	}
	
	@Override
	public void addXp(long xp) {
		xp = Math.abs(xp);
		long delta = getXpUntilNextLevel() - xp;
		
		if(delta > 0){
			this.xp += xp;
		} else {
			level++;
			xp = -delta;
		}
	}

	@Override
	public PlayerAchievementState getAchievementState(@NonNull PlayerAchievement achievement) {
		String name = achievement.getAchievementName().toLowerCase();
		
		if(kits.containsKey(name))
			return achievements.get(name);
		else {
			PlayerAchievementState state = new PlayerAchievementState();
			achievements.put(name, state);
			
			return state;
		}
	}

	@Override
	public int getUnlockedKitLevel(@NonNull PlayerKit kit) {
		String name = kit.getKitName().toLowerCase();
		
		if(kits.containsKey(name))
			return kits.get(name);
		else return 0;
	}

	@Override
	public boolean canUnlockNextLevel(@NonNull PlayerKit kit) {
		int nextLevel = getUnlockedKitLevel(kit) + 1;
		
		if(kit.getMaxLevel() < nextLevel || kit.getBadcoinsCost(nextLevel) < badcoins)
			return false;
		
		for(PlayerAchievement achievement : kit.getNeededAchievements(nextLevel)){
			if(!getAchievementState(achievement).isSucceeds())
				return false;
		}
		
		return true;
	}
	
	@Override
	public String getLastUsedKit(@NonNull String game) {
		game = game.toLowerCase();
		return lastUsedKits.containsKey(game) ? lastUsedKits.get(game) : null;
	}
	
	@Override
	public void setLastUsedKit(@NonNull String game, String kit) {
		game = game.toLowerCase();
		kit  = kit == null ? null : kit.toLowerCase();
		
		
		if(kit == null && lastUsedKits.containsKey(game)){
			lastUsedKits.remove(game);
		} else if(kit != null){
			lastUsedKits.put(game, kit);
		}
	}

	@Override
	public Locale getLocale() {
		return Locale.FRENCH_FRANCE; //TODO changer ça
	}

	@Override
	public double getStatistics(String gameName, String stat) {
		stat = stat.toLowerCase();
		Map<String, Double> gameStats = getGameStats(gameName);
		
		if(!gameStats.containsKey(stat)){
			gameStats.put(stat, 0d);
		}
		
		return gameStats.get(stat);
	}
	
	@Override
	public void incrementStastic(String gameName, String stat){
		augmentStatistic(gameName, stat, 1.0d);
	}
	
	@Override
	public void augmentStatistic(String gameName, String stat, double value){
		stat = stat.toLowerCase();
		Map<String, Double> gameStats = getGameStats(gameName);
		
		if(!gameStats.containsKey(stat)){
			gameStats.put(stat, 0d);
		}
		
		double newValue = value + gameStats.get(stat);
		gameStats.put(stat, newValue);
	}
	
	protected Map<String, Double> getGameStats(String gameName){
		gameName = gameName.toLowerCase();
		
		if(!stats.containsKey(gameName)){
			stats.put(gameName, Maps.newConcurrentMap());
		}
		
		return stats.get(gameName);
	}

	@SuppressWarnings("unchecked") @Override
	public <T extends PlayerData> T gameData(String key, Class<T> clazz) {
		key = key.toLowerCase();
		
		T result = null;
		
		if(datas.containsKey(key)){
			result = (T) datas.get(key);
		} else if(data.has(key)){
			result = new Gson().fromJson(data.get(key), clazz);
			datas.put(key, result);
		} else
			try {
				result = clazz.getConstructor().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return result;
	}

	@Override
	public void saveData() {
		JsonObject object = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create().toJsonTree(this).getAsJsonObject();
	
		for(Entry<String, PlayerData> entries : datas.entrySet()){
			if(!object.has(entries.getKey())){
				object.add(entries.getKey(), new Gson().toJsonTree(entries.getValue()));
			}
		}
	}
}

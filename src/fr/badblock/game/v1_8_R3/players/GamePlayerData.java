package fr.badblock.game.v1_8_R3.players;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.achievements.PlayerAchievement;
import fr.badblock.gameapi.players.data.PlayerAchievementState;
import fr.badblock.gameapi.players.data.PlayerData;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.utils.i18n.Locale;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter@ToString
public class GamePlayerData implements PlayerData {
	private int  				 						  badcoins     	   = 0;
	private int  				 						  level	     	   = 1;
	private long 										  xp		       = 0L;
	private int  				 						  coinsBonus   	   = 1;
	private int  				 						  xpBonus  	 	   = 1;
	private Map<String, Integer> 						  kits 		 	   = Maps.newConcurrentMap();
	private Map<String, String>							  lastUsedKits 	   = Maps.newConcurrentMap();
	private Map<String, PlayerAchievementState> 		  achievements 	   = Maps.newConcurrentMap();
	
	private Map<String, Map<String, Double>> 			  stats   	 	   = Maps.newConcurrentMap();
	
	private transient List<String>						  achloadeds	   = new ArrayList<>();
	
	private transient Map<String, PlayerData> 			  datas 		   = Maps.newConcurrentMap();
	private transient JsonObject 						  data		 	   = new JsonObject();
	private transient JsonObject 						  object		   = new JsonObject();

	public void setData(JsonObject data){
		if(data.has("other")){
			this.data = data.get("other").getAsJsonObject();
		}
	}
	
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
		long delta = getXpUntilNextLevel() - (xp + this.xp);
		
		if(delta > 0){
			this.xp += xp;
		} else {
			level++;
			this.xp = -delta;
		}
	}

	@Override
	public PlayerAchievementState getAchievementState(@NonNull PlayerAchievement achievement) {
		String name = achievement.getName().toLowerCase();
		
		if(achievements.containsKey(name)){
			PlayerAchievementState ach = achievements.get(name);
			if(achievement.isTemp() && !achloadeds.contains(name) && !ach.isSucceeds()){
				ach.setProgress(0.0d);
			}
			
			return ach;
		} else {
			PlayerAchievementState state = new PlayerAchievementState();
			achievements.put(name, state);
			achloadeds.add(name);
			
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
		
		if(kit.getMaxLevel() < nextLevel || kit.getBadcoinsCost(nextLevel) > badcoins)
			return false;
		
		for(PlayerAchievement achievement : kit.getNeededAchievements(nextLevel)){
			if(!getAchievementState(achievement).isSucceeds())
				return false;
		}
		
		return true;
	}
	
	@Override
	public void unlockNextLevel(@NonNull PlayerKit kit){
		if(!canUnlockNextLevel(kit)) return;
		
		int nextLevel = getUnlockedKitLevel(kit) + 1;
		
		removeBadcoins(kit.getBadcoinsCost(nextLevel));
		kits.put(kit.getKitName().toLowerCase(), nextLevel);
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
	public void incrementStatistic(String gameName, String stat){
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
			result = GameAPI.getGson().fromJson(data.get(key), clazz);
			datas.put(key, result);
		} else
			try {
				result = createObjectInstance(clazz);
				datas.put(key, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createObjectInstance(Class<T> c){
		if(c.isEnum() || c.isArray()){ // Could not load an Enum or an Array from an JObject
			return null;
		} else if(c.isInterface()){

		} else {
			try{
				return getConstructor(c).newInstance();
			} catch(Exception e){}
			try {
				Class<?> unsafe = Class.forName("sun.misc.Unsafe");
				Field f = unsafe.getDeclaredField("theUnsafe"); f.setAccessible(true);
				return (T) unsafe.getMethod("allocateInstance", Class.class).invoke(f.get(null), c);
			} catch (Exception e){}
		}
		return null;
	}
	
	private static <T> Constructor<T> getConstructor(Class<T> c){
		try {
			return c.getDeclaredConstructor();
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Can not get the default constructor of " + c.getSimpleName() + ".", e);
		} catch(SecurityException e){
			throw new RuntimeException("Can not get the default constructor of " + c.getSimpleName() + ".", e);
		}
	}

	@Override
	public void saveData() {
		JsonObject object = GameAPI.getGson().toJsonTree(this).getAsJsonObject();
	
		for(Entry<String, PlayerData> entries : datas.entrySet()){
			if(!object.has(entries.getKey())){
				object.add(entries.getKey(), GameAPI.getGson().toJsonTree(entries.getValue()));
			}
		}
	}
}

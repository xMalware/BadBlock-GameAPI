

package fr.badblock.game.core18R3.players.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Sound;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.achievements.PlayerAchievement;
import fr.badblock.gameapi.players.BadblockPlayer;
import fr.badblock.gameapi.players.data.GameData;
import fr.badblock.gameapi.players.data.PlayerAchievementState;
import fr.badblock.gameapi.players.data.PlayerData;
import fr.badblock.gameapi.players.data.boosters.PlayerBooster;
import fr.badblock.gameapi.players.kits.PlayerKit;
import fr.badblock.gameapi.utils.i18n.Locale;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter@ToString
public class GamePlayerData implements PlayerData {
	public static final transient Locale DEFAULT_LANGUAGE = Locale.FRENCH_FRANCE;

	public Locale								  		  locale	       = DEFAULT_LANGUAGE;

	private int  				 						  badcoins     	   = 0;
	public int  				 						  shopPoints       = 0;
	private int  				 						  level	     	   = 1;
	private long 										  xp		       = 0L;
	private List<PlayerBooster>					  		  boosters		   = new ArrayList<>();
	private Map<String, Integer> 						  kits 		 	   = Maps.newConcurrentMap();
	private Map<String, String>							  lastUsedKits 	   = Maps.newConcurrentMap();
	private Map<String, PlayerAchievementState> 		  achievements 	   = Maps.newConcurrentMap();

	private Map<String, Map<String, Double>> 			  stats   	 	   = Maps.newConcurrentMap();

	private transient List<String>						  achloadeds	   = new ArrayList<>();

	private transient Map<String, GameData> 			  datas 		   = Maps.newConcurrentMap();
	private transient JsonObject 						  data		 	   = new JsonObject();
	private transient JsonObject 						  object		   = new JsonObject();

	@Getter@Setter
	private transient GameBadblockPlayer				  gameBadblockPlayer;
	public transient long								  onlyJoinWhileWaiting;

	// temporary values
	private transient int 								  addedBadcoins	   = 0;
	private transient int								  addedShopPoints  = 0;
	private transient int								  addedLevels      = 0;
	private transient long								  addedXP		   = 0;
	private transient int 								  addedRankedPoints= 0;

	public void setData(JsonObject data){
		if(data.has("other")){
			this.data = data.get("other").getAsJsonObject();
		}
		if (this.xp < 0) this.xp = 0;
		if (this.badcoins < 0) this.badcoins = 0;
	}

	@Override
	public int addBadcoins(int badcoins, boolean applyBonus) {
		if (badcoins < 0) return 0;
		badcoins = Math.abs(badcoins);
		if (applyBonus) {
			GameAPI api = GameAPI.getAPI();
			double playerBonus = 0;
			PlayerBooster playerBooster = GamePlugin.getInstance().getBooster();
			if (playerBooster != null && playerBooster.getBooster() != null) {
				playerBonus += playerBooster.getBooster().getCoinsMultiplier();
			}
			if (playerBonus == 0) playerBonus = 1;
			double serverBonus = api.getServerBadcoinsBonus() <= 0 ? 1 : api.getServerBadcoinsBonus();
			badcoins *= serverBonus > playerBonus ? serverBonus : playerBonus;
			double v = 1;
			try {
				if (this.getGameBadblockPlayer() != null) {
					Double o = this.getGameBadblockPlayer().getPermissionValue("badcoinsboost", Double.class);
					if (o == null) o = 1.0d;
					v = o;
				}else System.out.println("null gamePlayer");
			}catch(Exception error) {
				error.printStackTrace();
				v = 1;
			}
			badcoins *= v < 1.0d ? 1.0d : v;
		}
		addedBadcoins += badcoins;
		return this.badcoins += badcoins;
	}

	@Override
	public void removeBadcoins(int badcoins) {
		// removing badcoins is only for buy, so must not be count in addedBadcoins
		//addedBadcoins -= Math.abs(badcoins);
		this.badcoins -= Math.abs(badcoins);
	}

	@Override
	public long getXpUntilNextLevel() {
		long base = 200;
		long add  = 50;

		for(int i=1;i<level;i++){
			base += add;
			add  += 20;
		}

		//Double doublet = Math.pow(1.2d, level + 1) * 100;
		//return doublet.longValue();
		return base;
	}

	@Override
	public long addXp(long xp, boolean applyBonus) {
		if (xp < 0) return 0;
		xp = Math.abs(xp);

		if (applyBonus) {
			double playerBonus = 0;
			PlayerBooster playerBooster = GamePlugin.getInstance().getBooster();
			if (playerBooster != null && playerBooster.getBooster() != null) {
				playerBonus += playerBooster.getBooster().getXpMultiplier();
			}
			if (playerBonus == 0) playerBonus = 1;
			double serverBonus = GameAPI.getAPI().getServerXpBonus() <= 0 ? 1 : GameAPI.getAPI().getServerXpBonus();
			xp *= serverBonus > playerBonus ? serverBonus : playerBonus;
			double v = 1;
			try {
				if (this.getGameBadblockPlayer() != null) {
					Double o = this.getGameBadblockPlayer().getPermissionValue("xpboost", Double.class);
					if (o == null) o = 1.0d;
					v = o;
				}
			}catch(Exception error) {
				v = 1;
			}
			xp *= v < 1.0d ? 1.0d : v;
		}

		addedXP += xp;
		// Gestion de l'XP
		long delta = getXpUntilNextLevel() - (xp + this.xp);
		this.xp += xp;
		// pas de passage de niveau
		if (delta > 0) return this.xp;
		// passage de niveau jusqu'à ce qu'il y ai suffisament de niveau(x) passé(s) pour avoir une progression
		while (getXpUntilNextLevel() - (this.xp + xp) <= 0) {
			level++;
			addedLevels++;
		}
		this.xp = 0;

		if (this.getGameBadblockPlayer() != null) {
			this.getGameBadblockPlayer().sendTranslatedMessage("game.level", level);
			this.getGameBadblockPlayer().playSound(Sound.LEVEL_UP);
		}

		return this.xp;
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
	public void incrementAchievements(BadblockPlayer player, PlayerAchievement... achievements) {
		for(PlayerAchievement achievement : achievements){
			PlayerAchievementState state = getAchievementState(achievement);
			state.progress(1.0d);
			state.trySucceed(player, achievement);
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
		increaseStatistic(gameName, stat, 1.0d);
	}

	@Override
	public void increaseStatistic(String gameName, String stat, double value){
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
	public <T extends GameData> T gameData(String key, Class<T> clazz) {
		key = key.toLowerCase();

		T result = null;

		if(datas.containsKey(key)){
			result = (T) datas.get(key);
		} else if(data.has(key)){
			//System.out.println(data.get(key));
			result = GameAPI.getGson().fromJson(data.get(key), clazz);
			datas.put(key, result);
		} else
			try {
				result = clazz.getConstructor().newInstance();
				datas.put(key, result);
			} catch (Exception e) {
				e.printStackTrace();
			}

		return result;
	}

	@Override
	public JsonObject saveData() {
		JsonObject object = GameAPI.getGson().toJsonTree(this).getAsJsonObject();
		for(Entry<String, GameData> entries : datas.entrySet()){
			if(data.has(entries.getKey())){
				data.remove(entries.getKey());
			}

			data.add(entries.getKey(), GameAPI.getGson().toJsonTree(entries.getValue()));
		}

		JsonObject result = new JsonObject();

		result.addProperty("onlyJoinWhileWaiting", onlyJoinWhileWaiting);
		object.add("other", data);
		result.add("game", object);

		return result;
	}

	@Override
	public int getShopPoints() {
		return shopPoints;
	}

	@Override
	public long addShopPoints(long shopPoints) {
		this.shopPoints += shopPoints;
		addedBadcoins += shopPoints;
		// envoi du packet d'update
		if (getGameBadblockPlayer().isDataFetch())
			GameAPI.getAPI().getLadderDatabase().updatePlayerData(getGameBadblockPlayer(), this.getObject());
		return this.shopPoints;
	}

	@Override
	public long removeShopPoints(long shopPoints) {
		this.shopPoints -= shopPoints;
		addedShopPoints -= shopPoints;
		// envoi du packet d'update
		if (getGameBadblockPlayer().isDataFetch())
			GameAPI.getAPI().getLadderDatabase().updatePlayerData(getGameBadblockPlayer(), this.getObject());
		return this.shopPoints;
	}

	@Override
	public int addRankedPoints(int rankedPoints) {
		if (!GamePlugin.getInstance().getGameServerManager().getRankedConfig().ranked) return addedRankedPoints;
		addedRankedPoints += rankedPoints;
		return addedRankedPoints;
	}

	@Override
	public int removeRankedPoints(int rankedPoints) {
		if (!GamePlugin.getInstance().getGameServerManager().getRankedConfig().ranked) return addedRankedPoints;
		addedRankedPoints -= rankedPoints;
		return addedRankedPoints;
	}

	@Override
	public double getBadcoinsMultiplier() {
		double multiplier = 1;
		PlayerBooster playerBooster = GamePlugin.getInstance().getBooster();
		if (playerBooster != null && playerBooster.getBooster() != null) {
			multiplier += playerBooster.getBooster().getCoinsMultiplier();
		}
		multiplier += GameAPI.getAPI().getServerXpBonus() <= 0 ? 0 : (GameAPI.getAPI().getServerBadcoinsBonus() - 1);
		double v = 1;
		try {
			if (this.getGameBadblockPlayer() != null) {
				Double o = this.getGameBadblockPlayer().getPermissionValue("badcoinsboost", Double.class);
				if (o == null) o = 1.0d;
				v = o;
			}
		}catch(Exception error) {
			v = 1;
		}
		multiplier += v;
		return multiplier;
	}

	@Override
	public double getXpMultiplier() {
		double multiplier = 1;
		PlayerBooster playerBooster = GamePlugin.getInstance().getBooster();
		if (playerBooster != null && playerBooster.getBooster() != null) {
			multiplier += playerBooster.getBooster().getXpMultiplier();
		}
		multiplier += GameAPI.getAPI().getServerXpBonus() <= 0 ? 0 : (GameAPI.getAPI().getServerXpBonus() - 1);
		double v = 1;
		try {
			if (this.getGameBadblockPlayer() != null) {
				Double o = this.getGameBadblockPlayer().getPermissionValue("xpboost", Double.class);
				if (o == null) o = 1.0d;
				v = o;
			}
		}catch(Exception error) {
			v = 1;
		}
		multiplier += v;
		return multiplier;
	}

}


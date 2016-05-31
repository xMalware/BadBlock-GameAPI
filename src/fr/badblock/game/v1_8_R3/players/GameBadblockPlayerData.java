package fr.badblock.game.v1_8_R3.players;

import java.util.Map;
import java.util.UUID;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockPlayerData;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.players.data.PlayerData;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import lombok.Getter;
import lombok.Setter;

public class GameBadblockPlayerData implements BadblockPlayerData {
	@Getter private UUID   	     		 uniqueId;
	@Getter private String 	   	 		 name;
	@Getter private PlayerData   		 playerData;
	@Getter@Setter private BadblockTeam  team;
	@Getter private TranslatableString   groupPrefix;
	@Getter private TranslatableString   tabGroupPrefix;
	private Map<Class<?>, InGameData> 	 inGameData;
	
	public GameBadblockPlayerData(GameBadblockPlayer player){
		this.uniqueId    	= player.getUniqueId();
		this.name	     	= player.getName();
		this.playerData  	= player.getPlayerData();
		this.team        	= player.getTeam();
		this.groupPrefix 	= player.getGroupPrefix();
		this.tabGroupPrefix = player.getTabGroupPrefix();
		this.inGameData  	= player.getInGameData();
	}

	@Override
	public <T extends InGameData> T inGameData(Class<T> clazz) {
		try {
			if (!inGameData.containsKey(clazz)) {
				inGameData.put(clazz, (InGameData) clazz.getConstructor().newInstance());
			}

			return clazz.cast(inGameData.get(clazz));
		} catch (Exception e) {
			e.printStackTrace();
			GameAPI.logError("Invalid InGameData class (" + clazz + ") ! Return null.");
			return null;
		}
	}
}

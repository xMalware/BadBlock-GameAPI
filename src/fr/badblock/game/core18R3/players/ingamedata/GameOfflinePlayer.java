package fr.badblock.game.core18R3.players.ingamedata;

import java.util.Map;
import java.util.UUID;

import org.bukkit.World.Environment;

import com.google.gson.JsonObject;

import fr.badblock.game.core18R3.players.GameBadblockPlayer;
import fr.badblock.game.core18R3.players.data.GamePlayerData;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.players.scoreboard.CustomObjective;
import fr.badblock.gameapi.utils.i18n.TranslatableString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameOfflinePlayer implements BadblockOfflinePlayer {
	private UUID uniqueId = null;
	private String name = null;
	private CustomObjective customObjective = null;
	private Environment falseDimension = null;
	private BadblockTeam team = null;
	private Map<Class<?>, InGameData> inGameData = null;
	private GamePlayerData playerData = null;
	private JsonObject object = null;
	private TranslatableString groupPrefix = null;
	private TranslatableString tabGroupPrefix = null;

	public GameOfflinePlayer(GameBadblockPlayer player) {
		this.uniqueId = player.getUniqueId();
		this.name = player.getName();
		this.customObjective = player.getCustomObjective();
		this.falseDimension = player.getCustomEnvironment();
		this.team = player.getTeam();
		this.inGameData = player.getInGameData();
		this.playerData = player.getPlayerData();
		this.object = player.getObject();
		this.groupPrefix = player.getGroupPrefix();
	}

	@Override
	public <T extends InGameData> T inGameData(Class<T> clazz) {
		try {
			if (!inGameData.containsKey(clazz)) {
				inGameData.put(clazz, clazz.getConstructor().newInstance());
			}

			return clazz.cast(inGameData.get(clazz));
		} catch (Exception e) {
			e.printStackTrace();
			GameAPI.logError("Invalid InGameData class (" + clazz + ") ! Return null.");
			return null;
		}
	}
}

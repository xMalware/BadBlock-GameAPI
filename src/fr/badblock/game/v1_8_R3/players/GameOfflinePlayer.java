package fr.badblock.game.v1_8_R3.players;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World.Environment;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.players.BadblockOfflinePlayer;
import fr.badblock.gameapi.players.BadblockTeam;
import fr.badblock.gameapi.players.data.InGameData;
import fr.badblock.gameapi.utils.CustomObjective;
import lombok.Getter;

public class GameOfflinePlayer implements BadblockOfflinePlayer {
	@Getter
	private BadblockTeam				 team				  = null;
	@Getter
	private Map<Class<?>, InGameData> 	 inGameData  		  = null;
	@Getter
	private GamePlayerData 				 playerData 		  = null;
	@Getter
	private JsonObject					 object				  = null;
	
	@Override
	public UUID getUniqueId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLastLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Environment getFalseDimension() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomObjective getCustomObjective() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends InGameData> T inGameData(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}

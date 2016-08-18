package fr.badblock.game.core18R3.players.data;

import com.google.gson.JsonObject;

import fr.badblock.gameapi.players.data.BoosterData;
import fr.badblock.gameapi.players.data.GameData;
import lombok.Getter;
import lombok.ToString;

@Getter@ToString
public class GameBoosterData implements BoosterData {

	private long 	expireTime;
	private int		coinsMultiplier		= 1;
	private int		XPMultiplier		= 1;

	@Override
	public boolean isExpired() {
		return !this.isValid();
	}

	@Override
	public boolean isValid() {
		return this.getExpireTime() > System.currentTimeMillis();
	}
	
}

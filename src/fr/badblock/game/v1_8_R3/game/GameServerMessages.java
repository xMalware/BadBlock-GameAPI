package fr.badblock.game.v1_8_R3.game;

import fr.badblock.gameapi.GameAPI;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xMalware
 */
@Getter public enum GameServerMessages {

	PREFIX("&b[GameServer] "),
	GAMESERVER_FILE_NOT_FOUND(PREFIX.toString() + "&cFile not found : gameServer.json"),
	SENDING_SHUTDOWN_SIGNAL(PREFIX.toString() + "&cSending a shutdown signal"),
	GAMESERVER_FILE_INVALID(PREFIX.toString() + "&cInvalid gameServer.json configuration"),
	UNLOADING(PREFIX.toString() + "&eUnloading..."),
	UNLOADED(PREFIX.toString() + "&aUnloaded!");
	
	@Setter private String message;
	
	GameServerMessages(String message) {
		this.setMessage(message);
	}
	
	@Override
	public String toString() {
		return this.getMessage();
	}
	
	public void log() {
		GameAPI.logColor(this.toString());
	}
	
}

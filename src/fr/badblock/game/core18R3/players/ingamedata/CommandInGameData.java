package fr.badblock.game.core18R3.players.ingamedata;

import org.bukkit.Location;

import fr.badblock.gameapi.players.data.InGameData;

public class CommandInGameData implements InGameData {
	public boolean invsee = false;
	public boolean vanish = false;
	public boolean godmode = false;
	public Location lastLocation;
}

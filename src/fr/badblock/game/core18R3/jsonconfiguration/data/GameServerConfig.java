package fr.badblock.game.core18R3.jsonconfiguration.data;

import fr.badblock.gameapi.run.RunType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameServerConfig {

	// GameServer part
	public boolean deleteFiles;
	public int timeBetweenLogs;
	public int ticksBetweenMonitoreLogs;
	public int ticksBetweenKeepAlives;
	public long uselessUntilTime;
	public boolean leaverBusterEnabled;
	
	// Run type
	public RunType runType = RunType.LOBBY;

}
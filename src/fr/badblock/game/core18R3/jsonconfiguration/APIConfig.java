package fr.badblock.game.core18R3.jsonconfiguration;

import fr.badblock.gameapi.run.RunType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class APIConfig {
	
	// Ladder part
	public String 	ladderIp = "";
	public int	  	ladderPort;

	// SQL Part
	public String 	sqlIp 		= "";
	public String   sqlUser		= "root";
	public int    	sqlPort 	= 3306;
	public String 	sqlDatabase = "";
	public String 	sqlPassword = "";
	
	// FTP part
	public String 	ftpHostname = "";
	public String 	ftpUsername = "";
	public String 	ftpPassword = "";
	public int	  	ftpPort;
	
	// Rabbit part
	public String	rabbitHostname;
	public int		rabbitPort;
	public String	rabbitUsername;
	public String	rabbitPassword;
	public String	rabbitVirtualHost;
	
	// GameServer part
	public boolean	deleteFiles;
	public int		timeBetweenLogs;
	public int		ticksBetweenMonitoreLogs;
	public int		ticksBetweenKeepAlives;
	public long		uselessUntilTime;
	
	public RunType  runType = RunType.LOBBY;
	
}
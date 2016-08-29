package fr.badblock.game.core18R3.jsonconfiguration.data;

import fr.badblock.gameapi.run.RunType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SQLConfig {
	
	// SQL Part
	public String sqlIp = "";
	public String sqlUser = "root";
	public int sqlPort = 3306;
	public String sqlDatabase = "";
	public String sqlPassword = "";

}
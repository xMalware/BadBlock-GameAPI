package fr.badblock.game.core18R3.jsonconfiguration.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FTPConfig {

	// FTP part
	public String ftpHostname = "";
	public String ftpUsername = "";
	public String ftpPassword = "";
	public int ftpPort;

}
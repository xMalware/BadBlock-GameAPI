package fr.badblock.game.v1_8_R3.game.threading;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

import fr.badblock.game.v1_8_R3.GamePlugin;
import fr.badblock.game.v1_8_R3.jsonconfiguration.APIConfig;

public class GameServerSendLogsThread {
	
	private APIConfig	config;
	
	public GameServerSendLogsThread(APIConfig config) {
		this.config = config;
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {	
				doLog();
			}
		}, 0, 60000);
	}
	
	public void doLog() {
		File file = new File("logs/latest.log");
		if (file.exists()) {
			FTPSClient ftpClient = new FTPSClient("TLS");
			try {
				ftpClient.connect(config.ftpHostname, config.ftpPort);
				ftpClient.login(config.ftpUsername, config.ftpPassword);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.execPBSZ(0); 
				ftpClient.execPROT("P");
				String logFile = "/logs/" + GamePlugin.getInstance().getGameServerManager().getLogsFile();
				String[] splitter = logFile.split("/");
				String old = "";
				int nb = splitter.length;
				int i = 0;
				for (String split : splitter) {
					i++;
					if (i == nb) break;
					old += split + "/";
					ftpClient.makeDirectory(old);
				}
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				String string = "";
				while ((line = br.readLine()) != null) {
					string += line.replaceAll("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})", "REGEX_IP").replaceAll("^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$", "REGEX_URL") + System.lineSeparator();
				}
				br.close();
				fis.close();
				InputStream stream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
				ftpClient.storeFile(logFile, stream);
				stream.close();
				ftpClient.disconnect();
			}catch(Exception error) {
				error.printStackTrace();
			}
		}else{
			System.out.println("Unknown log file. (" + file.getAbsolutePath() + ")");
		}
	}

}

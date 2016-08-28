package fr.badblock.game.core18R3.game.threading;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

import fr.badblock.game.core18R3.jsonconfiguration.APIConfig;

public class GameServerSendLogsTask extends GameServerTask {

	private APIConfig config;
	private String logFile;

	public GameServerSendLogsTask(APIConfig config) {
		this.config = config;
		new Timer().schedule(this, 0, config.timeBetweenLogs);
		// Temporary
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("server.properties"), Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String line : lines) {
			if (line.startsWith("docker-logs=")) {
				logFile = line.replace("docker-logs=", "");
			}
		}
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
				// ServerConfigurationFactory serverConfigurationFactory =
				// GamePlugin.getInstance().getGameServerManager().getServerConfigurationFactory();

				/*
				 * String[] splitt =
				 * GameAPI.getAPI().getServer().getServerName().split("_");
				 * String af; if (splitt.length < 2) af = "1"; else af =
				 * GameAPI.getAPI().getServer().getServerName().split("_")[1];
				 * String prefix =
				 * GameAPI.getAPI().getServer().getServerName().split("_")[0];
				 * long serverId = Integer.parseInt(af);
				 */

				String logFile = "/logs/" + this.logFile;
				String[] splitter = logFile.split("/");
				String old = "";
				int nb = splitter.length;
				int i = 0;
				for (String split : splitter) {
					i++;
					if (i == nb)
						break;
					old += split + "/";
					ftpClient.makeDirectory(old);
				}
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				String string = "";
				while ((line = br.readLine()) != null) {
					string += line.replaceAll("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})", "REGEX_IP")
							.replaceAll("^((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}$", "REGEX_URL")
							+ System.lineSeparator();
				}
				br.close();
				fis.close();
				InputStream stream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
				ftpClient.storeFile(logFile, stream);
				stream.close();
				ftpClient.disconnect();
			} catch (Exception error) {
				error.printStackTrace();
			}
		} else {
			System.out.println("Unknown log file. (" + file.getAbsolutePath() + ")");
		}
	}

	@Override
	public void run() {
		doLog();
	}

}

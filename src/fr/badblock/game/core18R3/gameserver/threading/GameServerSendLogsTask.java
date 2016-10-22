package fr.badblock.game.core18R3.gameserver.threading;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Timer;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import fr.badblock.game.core18R3.jsonconfiguration.data.FTPConfig;
import fr.badblock.game.core18R3.jsonconfiguration.data.GameServerConfig;
import fr.badblock.gameapi.utils.ServerProperties;

public class GameServerSendLogsTask extends GameServerTask {

	private FTPConfig config;
	private String logFile;

	public GameServerSendLogsTask(GameServerConfig gameServerConfig, FTPConfig ftpConfig) {
		this.config = ftpConfig;
		new Timer().schedule(this, 0, gameServerConfig.timeBetweenLogs);
		// Temporary
		/*
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
		}*/
		logFile = ServerProperties.getProperties().getProperty("docker-logs");
	}

	public void doLog() {
		File file = new File("./logs/latest.log");
		if (file.exists()) {
			FTPClient ftpClient = new FTPClient();
			try {
				ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
				ftpClient.connect(config.ftpHostname, config.ftpPort);
				ftpClient.login(config.ftpUsername, config.ftpPassword);
				ftpClient.setBufferSize(0);
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				ftpClient.setAutodetectUTF8(true);
				ftpClient.setListHiddenFiles(true);
				ftpClient.enterLocalActiveMode();
				
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
				boolean result = ftpClient.storeFile(logFile, stream);
				System.out.println(logFile + " / " + result + " / " + ftpClient.getReplyCode() + " / " + ftpClient.getReplyString());
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

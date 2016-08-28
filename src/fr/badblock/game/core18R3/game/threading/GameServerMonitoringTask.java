package fr.badblock.game.core18R3.game.threading;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.game.core18R3.game.GameServerManager;
import fr.badblock.game.core18R3.jsonconfiguration.APIConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class GameServerMonitoringTask extends GameServerTask {

	public GameServerMonitoringTask(APIConfig config) {
		TaskManager.scheduleAsyncRepeatingTask("gameServerMonitoring", this, 0, config.ticksBetweenMonitoreLogs);
	}

	public Number getMXBean(String methodName) {
		try {
			OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
			if (Class.forName("com.sun.management.OperatingSystemMXBean").isInstance(os)) {
				Method memorySize = os.getClass().getDeclaredMethod(methodName);
				memorySize.setAccessible(true);
				return (Number) memorySize.invoke(os);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getOpenedServers() {
		int openServers = 0;
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("screen -ls");
			try {
				process.waitFor();
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";

			while ((line = reader.readLine()) != null) {
				if (line.contains("("))
					openServers++;
			}
		} catch (Exception e) {
			return 0;
		}
		return openServers;
	}

	@Override
	public void run() {
		GamePlugin gamePlugin = GamePlugin.getInstance();
		GameServerManager gameServerManager = gamePlugin.getGameServerManager();

		long freeMemory = getMXBean("getFreePhysicalMemorySize").longValue() / (1024 * 1024);
		double cpuLoad = (getMXBean("getProcessCpuLoad").doubleValue() + getMXBean("getSystemCpuLoad").doubleValue())
				* 100.0D;

		GameAPI.logColor("&b[GameServer &6DEDICATED&b] &7Instance" + (getOpenedServers() > 1 ? "s" : "") + ": "
				+ getOpenedServers() + " | CPU: " + MathsUtils.round(cpuLoad, 2) + "% | FREEMEM: "
				+ MathsUtils.round(freeMemory, 2) + "MB");
		Bukkit.dispatchCommand(gameServerManager.getConsole(), "tps");
	}

}

package fr.badblock.game.core18R3.gameserver.threading;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

import fr.badblock.game.core18R3.jsonconfiguration.data.GameServerConfig;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.utils.general.MathsUtils;
import fr.badblock.gameapi.utils.threading.TaskManager;

public class GameServerMonitoringTask extends GameServerTask {

	public GameServerMonitoringTask(GameServerConfig config) {
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

	@Override
	public void run() {

		long freeMemory = getMXBean("getFreePhysicalMemorySize").longValue() / (1024 * 1024);
		double cpuLoad = (getMXBean("getProcessCpuLoad").doubleValue() + getMXBean("getSystemCpuLoad").doubleValue())
				* 100.0D;

		GameAPI.logColor("&b[GameServer &6DEDICATED&b] CPU: " + MathsUtils.round(cpuLoad, 2) + "% | FREEMEM: "
				+ MathsUtils.round(freeMemory, 2) + "MB");
	}

}

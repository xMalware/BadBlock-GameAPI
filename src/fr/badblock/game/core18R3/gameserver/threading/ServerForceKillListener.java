package fr.badblock.game.core18R3.gameserver.threading;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import org.bukkit.Bukkit;

import fr.badblock.game.core18R3.GamePlugin;
import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.run.RunType;
import fr.badblock.rabbitconnector.RabbitListener;
import fr.badblock.rabbitconnector.RabbitListenerType;

public class ServerForceKillListener extends RabbitListener {
	public ServerForceKillListener() {
		super(GamePlugin.getInstance().getRabbitSpeaker().getRabbitService(), "forcekill", false, RabbitListenerType.SUBSCRIBER);
	}

	@Override
	public void onPacketReceiving(String body) {
		if (body == null)
			return;
		
		if(body.equalsIgnoreCase(Bukkit.getServerName()) && GameAPI.getAPI().getRunType() == RunType.DEV)
		{
			String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
			System.out.println("Kill process...");
			
			try {
				new ProcessBuilder("kill", "-9", pid).start().waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

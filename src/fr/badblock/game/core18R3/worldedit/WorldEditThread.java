package fr.badblock.game.core18R3.worldedit;

import org.bukkit.scheduler.BukkitRunnable;

import fr.badblock.gameapi.GameAPI;
import fr.badblock.gameapi.worldedit.WEActionGroup;
import lombok.Getter;

public class WorldEditThread extends BukkitRunnable {
	@Getter
	private WEActionGroup action = new WEActionGroup();
	private int i = 0;
	
	@Override
	public void run() {
		WorldEditTimer timer = new WorldEditTimer(20L);
		
		while(action.hasNext() && !timer.timedOut)
			action.next();
		
		timer = null;
		i++;
		
		if(i == 40)
		{
			i = 0;
			
			if(action.getApplicant() != null)
			{
				long perc = (100 * action.getIterationCount()) / action.getTotalIterationCount();
				GameAPI.i18n().sendMessage(action.getApplicant(), "commands.worldedit.actionstate", perc);
			}
		}
	}
}

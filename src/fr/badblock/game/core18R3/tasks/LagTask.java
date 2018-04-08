package fr.badblock.game.core18R3.tasks;

import fr.badblock.gameapi.utils.threading.TaskManager;

public class LagTask implements Runnable {
	
	public LagTask() {
		TaskManager.scheduleSyncRepeatingTask("lagTask", this, 20, 20 * 30);
	}
	
	@Override
	public void run()
	{
		for (Thread thread : Thread.getAllStackTraces().keySet())
		{
			System.out.println(thread.getId() + " : " + thread.getName() + " [" + thread.getState() + "]");
		}
	}
	
}

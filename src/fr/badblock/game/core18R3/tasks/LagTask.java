package fr.badblock.game.core18R3.tasks;

import java.util.Map.Entry;

import fr.badblock.gameapi.utils.threading.TaskManager;

public class LagTask implements Runnable {
	
	public LagTask() {
		TaskManager.scheduleSyncRepeatingTask("lagTask", this, 20, 20 * 30);
	}
	
	@Override
	public void run() {
		for (Entry<String, Double> entry : TaskManager.tasksTime.entrySet())
		{
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}
	
}

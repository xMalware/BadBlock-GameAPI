package fr.badblock.game.core18R3.worldedit;

public class WorldEditTimer extends Thread {
	public boolean timedOut = false;
	private long time;
	
	public WorldEditTimer(long time) {
		this.time = time;
	}
	
	@Override
	public void run()
	{
		try {
			Thread.sleep(time);
			timedOut = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

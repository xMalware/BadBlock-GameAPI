package fr.badblock.bukkit.hub.tasks;

import fr.badblock.gameapi.utils.threading.TaskManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CustomTask {

	private int delay;
	private int repeat;

	public CustomTask(int delay, int repeat) {
		this.setDelay(delay);
		this.setRepeat(repeat);
		if (this.getDelay() == 0)
			done();
		TaskManager.scheduleSyncRepeatingTask(this.getClass().getSimpleName(), new Runnable() {
			@Override
			public void run() {
				done();
			}
		}, this.getDelay() == 0 ? this.getRepeat() : this.getDelay(), this.getRepeat());
	}

	public abstract void done();

}

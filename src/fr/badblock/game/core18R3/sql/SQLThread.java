package fr.badblock.game.core18R3.sql;

import java.util.Queue;

import com.google.common.collect.Queues;

/**
 * @author xMalware
 */
public class SQLThread extends Thread {
	
	private Queue<SQLRequest>	requests	= Queues.newLinkedBlockingDeque();
	
	public SQLThread(int id) {
		this.setName("BadBlockAPI/SQL/" + id);
		this.start();
	}
	
	@Override
	public void run() {
		synchronized (this) {
			while (true) {
				if (!requests.isEmpty()) {
					SQLRequest request = requests.poll();
					request.done();
				}
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean isAvailable() {
		return this.getState() != null && this.getState().equals(State.WAITING);
	}

	public void call(SQLRequest sqlRequest) {
		requests.add(sqlRequest);
		synchronized (this) {
			this.notify();
		}
	}
	
}

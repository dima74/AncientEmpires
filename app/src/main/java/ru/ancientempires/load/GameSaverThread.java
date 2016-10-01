package ru.ancientempires.load;

import java.util.concurrent.LinkedBlockingQueue;

import ru.ancientempires.framework.MyAssert;

public class GameSaverThread extends Thread {

	public LinkedBlockingQueue<Save>    queue        = new LinkedBlockingQueue<>();
	public LinkedBlockingQueue<Boolean> reverseQueue = new LinkedBlockingQueue<>(); // Void нельзя, ибо нельзя null

	public GameSaverThread() {
		setName("Saver Thread");
	}

	@Override
	public void run() {
		try {
			while (true) {
				int rc = queue.take().save();
				if (rc == 1)
					reverseQueue.add(true);
				else if (rc == 2)
					break;
			}
		} catch (Exception e) {
			MyAssert.a(false);
			e.printStackTrace();
		}
		MyAssert.a(queue.isEmpty());
	}

}

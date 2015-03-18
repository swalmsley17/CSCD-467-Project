package APEServer;

import java.io.IOException;
import java.net.ServerSocket;

public class APEServer implements Runnable {

	@Override
	public void run() {
		APEThreadMonitor monitor = new APEThreadMonitor();
		ServerSocket listener;
		APEThreadPool tp = new APEThreadPool(monitor);
		tp.startPool();
		Thread threadpool = new Thread(tp);
		threadpool.start();

		try {
			listener = new ServerSocket(15001);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		while (!Thread.interrupted()) {
			try {
				APEJob newjob = new APEJob(listener.accept());
				monitor.addJob(newjob);
			} catch (IOException | APEException e) {
				e.printStackTrace();
			}
		}
		try {
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

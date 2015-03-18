package APEServer;

import java.io.IOException;

public class APEThreadPool implements Runnable {
	private int maxCapacity;
	private APEThread[] holders;

	private int starting_threads;

	APEThreadMonitor jobQueue;

	public APEThreadPool(APEThreadMonitor monitor) {
		this.jobQueue = monitor;
		this.starting_threads = 20;
	}

	public void startPool() {
		this.holders = new APEThread[this.starting_threads];
		for (int i = 0; i < this.holders.length; i++) {
			this.holders[i] = new APEThread(this.jobQueue);
			this.holders[i].start();
		}
	}

	public void stopPool() throws IOException {
		System.out.println("Server has recieved a Kill message");
		System.out.println("Waiting for active jobs to finish");
		this.jobQueue.killRemainingJobs();
		for (APEThread t : this.holders)
			t.interrupt();
		for (APEThread t : this.holders)
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("All active jobs have finished gracefully");
	}

	public int numberThreadsRunning() {
		return this.holders.length;
	}

	public int maxCapacity() {
		return this.maxCapacity;
	}

	@Override
	public void run() {
		while(!Thread.interrupted())
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				try {
					this.stopPool();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	}
}

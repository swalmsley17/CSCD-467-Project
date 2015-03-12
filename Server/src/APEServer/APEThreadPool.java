package APEServer;
import java.io.IOException;

public class APEThreadPool implements Runnable {
	private int maxCapacity;
	private APEThread[] holders;

	private int v, t1, t2, t3, starting_threads;

	APEThreadMonitor jobQueue;

	public APEThreadPool(APEThreadMonitor monitor) {
		this.jobQueue = monitor;

		// CHANGE VALUES BASED ON TEST
		this.maxCapacity = 50;
		
		//MILLISECONDS
		this.v = 1;
		
		this.t1 = 1;
		this.t2 = 10;
		this.t3 = 20;
		this.starting_threads = 5;
	}

	public void startPool() {
		this.holders = new APEThread[this.starting_threads];
		for (int i = 0; i < this.holders.length; i++) {
			this.holders[i] = new APEThread(this.jobQueue);
			this.holders[i].start();
		}
	}

	public void increaseThreadsInPool() {
		APEThread[] new_holders = new APEThread[this.holders.length * 2];

		int i = 0;
		for (; i < this.holders.length; i++)
			new_holders[i] = this.holders[i];
		for (; i < new_holders.length; i++) {
			new_holders[i] = new APEThread(this.jobQueue);
			new_holders[i].start();
		}
		
		System.out.println("THREAD POOL INCREASED FROM " + this.holders.length + " TO " + new_holders.length);
		this.holders = new_holders;
	}

	public void decreaseThreadsInPool() {
		APEThread[] new_holders = new APEThread[this.holders.length / 2];

		int i = 0;
		for (; i < new_holders.length; i++)
			new_holders[i] = this.holders[i];
		for (; i < this.holders.length; i++)
			this.holders[i].interrupt();
		
		System.out.println("THREAD POOL DECREASED FROM " + this.holders.length + " TO " + new_holders.length);
		this.holders = new_holders;
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
		boolean t1_triggered = false;
		boolean t2_triggered = false;
		boolean t3_triggered = false;
		while (true) {
			int job_count = this.jobQueue.getSize();
			if (job_count > this.t1 && job_count <= this.t2) {
				if (!t1_triggered) {
					t1_triggered = true;
					this.increaseThreadsInPool();
				}
				if (t2_triggered) {
					t2_triggered = false;
					this.decreaseThreadsInPool();
				}
				if (t3_triggered) {
					t3_triggered = false;
					this.decreaseThreadsInPool();
				}
			} else if (job_count > this.t2 && job_count <= this.t3) {
				if (!t1_triggered) {
					t1_triggered = true;
					this.increaseThreadsInPool();
				}
				if (!t2_triggered) {
					t2_triggered = true;
					this.increaseThreadsInPool();
				}
				if (t3_triggered) {
					t3_triggered = false;
					this.decreaseThreadsInPool();
				}
			} else if (job_count > this.t3) {
				if (!t1_triggered) {
					t1_triggered = true;
					this.increaseThreadsInPool();
				}
				if (!t2_triggered) {
					t2_triggered = true;
					this.increaseThreadsInPool();
				}
				if (!t3_triggered) {
					t3_triggered = true;
					this.increaseThreadsInPool();
				}
			}

			try {
				Thread.sleep(this.v);
			} catch (InterruptedException e) {
				try {
					this.stopPool();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			}
		}
	}
}

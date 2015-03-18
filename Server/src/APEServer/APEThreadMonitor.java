package APEServer;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class APEThreadMonitor {
	private ArrayList<String> locks;
	private Queue<APEJob> jobqueue;

	public APEThreadMonitor() {
		this.locks = new ArrayList<String>();
		this.jobqueue = new LinkedList<APEJob>();
	}

	public synchronized void addJob(APEJob job) {
		this.jobqueue.add(job);
		this.notifyAll();
	}
	
	public synchronized APEJob getJob()  {
		while (this.jobqueue.size() == 0)
			try {
				this.wait();
			} catch (InterruptedException e) {
				return null;
			}
		return this.jobqueue.poll();
	}
	
	public synchronized void setLock(String lock_name) {
		if (locks.contains(lock_name))
			return;
		else
			locks.add(lock_name);
	}

	public synchronized int getSize() {
		return this.jobqueue.size();
	}

	public synchronized void killRemainingJobs() throws IOException {
		while (!this.jobqueue.isEmpty()) {
			APEJob j = this.jobqueue.poll();
			j.kill();
		}
	}

	public synchronized int getLockIndex(String name) {
		for (String s : this.locks) {
			if (s.equals(name))
				return this.locks.indexOf(s);
		}
		return -1;
	}

	public void doWork(String name, String code) throws APEException, IOException{
		int index = getLockIndex(name);
		if(index == -1)
			throw new APEException("Locking Error");
		synchronized(this.locks.get(index)) {
			SourceConverter.compileAndRun(code,name);
		}
	}
}

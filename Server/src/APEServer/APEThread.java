package APEServer;

import java.io.IOException;

public class APEThread extends Thread {

	private APEThreadMonitor monitor;
	public APEThread(APEThreadMonitor monitor) {
		this.monitor = monitor;
	}
	
	public void run() {
		APEJob job = null;
		while (!Thread.interrupted()) {
				String result = null;
				try {
					job = this.monitor.getJob();
					if(job == null)
						throw new InterruptedException();
					this.monitor.setLock(job.name);
					this.monitor.doWork(job.name, job.code);
					result = "Compiled, Ran, and Output Matched Successfully";
				} catch (APEException e) {
					System.out.println(e.getMessage());
					result = e.getMessage();
				} catch (IOException e) {
					result = "FILE I/O EXCEPTION";
				} catch (InterruptedException e) {
					return;
				}
				try {
					job.sendResult(result);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}

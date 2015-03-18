package APEServer;

import java.io.IOException;
import java.util.Random;

public class APEThread extends Thread {

	private APEThreadMonitor monitor;
	
	public APEThread(APEThreadMonitor monitor) {
		this.monitor = monitor;
	}
	
	public void run() {
		APEJob job = null;
		while (true) {
				String result = null;
				try {
					job = this.monitor.getJob();
					this.monitor.doWork(job.name, job.code);
					result = "Compiled, Ran, and Output Matched Successfully";
				} catch (APEException e) {
					System.out.println(e.getMessage());
					result = e.getMessage();
				} catch (InterruptedException e) {
					result = "Wow, Something REALLY BAD JUST HAPPENED";
				} catch (IOException e) {
					result = "FILE I/O EXCEPTION";
				}
				try {
					job.sendResult(result);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	private static int doOp() {

		return 0;
	}
}

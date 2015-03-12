package APEServer;

import java.util.Scanner;

public class ServerMain {

	public static void main(String[] args) throws InterruptedException {
		Thread server_thread = new Thread(new APEServer());
		server_thread.start();

		Scanner fin = new Scanner(System.in);
		String kill_message = "";
		while (true) {
			kill_message = fin.nextLine();
			if (kill_message.equals("kill"))
				break;
		}
		server_thread.interrupt();
		server_thread.join();

		fin.close();
	}

}

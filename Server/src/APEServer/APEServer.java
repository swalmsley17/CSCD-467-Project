package APEServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class APEServer implements Runnable {

	@Override
	public void run() {
		ServerSocket listener;
		try {
			listener = new ServerSocket(15001);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		while (!Thread.interrupted()) {
			try {
				Socket new_connection = listener.accept();
				DataInputStream is = new DataInputStream(
						new_connection.getInputStream());
				byte[] buffer = new byte[is.readInt()]; // read in the file size
														// first is.readInt()
				is.read(buffer);
				String code = new String(buffer);
				System.out.println("Received code:");
				System.out.println(code);

				// Run code dynamically

				String result = null;
				Method[] methods = null;
				try {
					methods = SourceConverter.textToRunnable(code);
				} catch (APEException e) {
					System.out.println(e.getMessage());
					result = e.getMessage();
				}
				if (result == null) {
					try {
						if (methods[0].getName().equals("main"))
							methods[0].invoke(null, (Object) new String[0]);
						result = APEClassTester.compareOutputFiles(methods[0]
								.getDeclaringClass().getName());
					} catch (Exception e) {
						result = "";
						System.out.println("Error running code:");
						result = e.getMessage();
					}
				}
				// Send Response back to client
				DataOutputStream os = new DataOutputStream(
						new_connection.getOutputStream());
				byte[] message = result.getBytes();
				os.writeInt(message.length);
				os.write(message);
				os.flush();
				System.out.print("Result: ");
				System.out.println(result + '\n');
				System.out.println("----------------------------------------------------");
			} catch (IOException e) {
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
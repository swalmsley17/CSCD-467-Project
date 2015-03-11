import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.io.InputStream;

public class APEServer implements Runnable {

	private final static int MAX_CAPACITY = 50;

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
				Method call = SourceConverter.textToRunnable(code);
				Object result = "";
				try {
					result = call.invoke(null,new String[0]);
				} catch (Exception e) {
					result = "";
					System.out.println("Error running code:");
					e.printStackTrace();
				}

				// Send Response back to client
				ObjectOutputStream os = new ObjectOutputStream(
						new_connection.getOutputStream());
				os.writeInt(result.length());
				os.writeBytes(result);
				os.flush();
				System.out.println("Result:");
				System.out.println(result);
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

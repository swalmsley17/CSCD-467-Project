package APEServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class APEJob {
	private Socket sock;
	public String code;
	public String name;

	public APEJob(Socket sock) throws IOException, APEException {
		this.sock = sock;
		this.setupJob();
	}

	public void setupJob() throws IOException, APEException {
		DataInputStream is = new DataInputStream(sock.getInputStream());
		byte[] buffer = new byte[is.readInt()];
		is.read(buffer);
		this.code = new String(buffer);
		System.out.println("Received code:");
		System.out.println(code);
		this.name = SourceConverter.getName(code);
	}

	public void kill() throws IOException {
		this.sock.close();
	}

	public void sendResult(String result) throws IOException {
		DataOutputStream os = new DataOutputStream(this.sock.getOutputStream());
		byte[] message = result.getBytes();
		os.writeInt(message.length);
		os.write(message);
		os.flush();
	}

}

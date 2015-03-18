package clientWindow;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;

import javax.swing.JTextArea;

public class Client {
	
	private ClientWindow clientWindow;
	private DataInputStream in;
	private DataOutputStream out;
	
	public Client(){
		
	}
	
	public void connectToServer(String serverAddress, String serverPort, String fileContents, ClientWindow serverMessageField){
		clientWindow = serverMessageField;
		clientWindow.receiveServerMessage("Attempting connect: IP:" + serverAddress + ", Port: " + serverPort);

		try {
			//create socket to server
			Socket socket = new Socket(serverAddress, Integer.parseInt(serverPort));

			//create data streams
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			clientWindow.receiveServerMessage("Connected to server.");
			
			//create byte array from passed in file and send data
			byte[] data = fileContents.getBytes();
			out.writeInt(data.length);
			out.write(data);
			
			//read in from the server feedback and show user
			byte[] buffer = new byte[in.readInt()];
			in.read(buffer);
			clientWindow.receiveServerMessage(new String(buffer));
         in.close();
			out.close();
			socket.close();
			
			//let the user know the connection has been closed
			clientWindow.receiveServerMessage("Connection closed.");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			clientWindow.receiveServerMessage("Error occured when connecting to server, check the entered IP and port and try again later.");
			e.printStackTrace();
		}
	}
}

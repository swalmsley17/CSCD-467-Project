package clientWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

public class Client {
	
	private ClientWindow clientWindow;
	private BufferedReader in;
	private PrintWriter out;
	
	public Client(){
		
	}
	
	public void connectToServer(String serverAddress, String serverPort, File file, ClientWindow serverMessageField){
		clientWindow = serverMessageField;
		clientWindow.receiveServerMessage("Attempting connect: IP:" + serverAddress + ", Port: " + serverPort + "...");

		try {
			Socket socket = new Socket(serverAddress, Integer.parseInt(serverPort));
			in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			clientWindow.receiveServerMessage("Connected to server.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			clientWindow.receiveServerMessage("Error occured when connecting to server, check the entered IP and port and try again later.");
			e.printStackTrace();
		}
	}
	
	public void sendData(){
		//INCOMPLETE
	}
}

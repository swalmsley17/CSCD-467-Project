package server_tester;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerTester {

	final static String server_address = "127.0.0.1";

	final static String test_one = "public class TestOne {\n\n \n    public TestOne(String in) {\n      System.out.println(in);\n    }\n}";

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		testOne();
	}

	public static void testOne() throws UnknownHostException, IOException {
		Socket sock = new Socket(server_address, 15001);

		DataOutputStream out = new DataOutputStream(sock.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(
				sock.getInputStream()));
		byte[] data = test_one.getBytes();
		out.writeInt(test_one.getBytes().length);
		out.write(data);
		System.out.println(in.readLine());
	}
}

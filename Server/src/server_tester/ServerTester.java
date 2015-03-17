package server_tester;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerTester {

	final static String server_address = "127.0.0.1";

	// standard hello world main
	final static String test_one = "\npublic class TestOne {\n\u0009public static void main(String[] args) {\n\u0009\u0009System.out.println(\"Hello World!\");\n\u0009}\n}\n";

	// calls other methods in class
	final static String test_two = "\npublic class TestTwo {\n\n\u0009public static void main(String[] args) {\n\u0009\u0009methodOne(\"this_is_method_one\");\n\u0009}\n\n\u0009private static void methodOne(String message) {\n\u0009\u0009System.out.println(message);\n\u0009\u0009methodTwo(\"this_is_method_two\");\n\u0009}\n\n\u0009private static void methodTwo(String message) {\n\u0009\u0009System.out.println(message);\n\u0009}\n\n}\n";

	final static String bad_output = "public class BadOutput {\n\u0009public static void main(String[] args) {\n\u0009\u0009System.out.println(\"Hello World?\");\n\u0009}\n}\n";
	final static String compile_error = "public class CompileErrur {\nperblic stotic vod meen(Sting(] blargs) {\n]\n}";
	final static String runtime_error = "";
	final static String hanging_program = "";

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		System.out.print("Test One: ");
		runTest(test_one);
		System.out.println();
		
		System.out.print("Test Two: ");
		runTest(test_two);
		System.out.println();
		
		System.out.print("Compile Error: ");
		runTest(compile_error);
		System.out.println();
		
		System.out.print("Bad Output: ");
		runTest(bad_output);
		System.out.println();
	}

	public static void runTest(String test) throws UnknownHostException,
			IOException {
		Socket sock = new Socket(server_address, 15001);

		DataOutputStream os = new DataOutputStream(sock.getOutputStream());
		DataInputStream is = new DataInputStream(sock.getInputStream());

		byte[] data = test.getBytes();
		os.writeInt(test.getBytes().length);
		os.write(data);

		byte[] buffer = new byte[is.readInt()];
		is.read(buffer);

		System.out.println(new String(buffer));
		sock.close();
	}
}

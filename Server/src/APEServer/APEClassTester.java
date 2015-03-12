package APEServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;

public class APEClassTester {
	final static String KEY_ADDENDUM = "_key.txt";
	final static String OUTPUT_ADDENDUM = "_output.txt";
	public static String staticMethodTester(Method[] methods) {
		return null;
	}

	public static String mainTester(Method[] methods) {
		return null;
	}

	public static String compareOutputFiles(String classname) {
		String s1 = "";
		String s3 = "";
		String y = "", z = "";

		File file1 = new File(classname + KEY_ADDENDUM);
		File file2 = new File(classname + OUTPUT_ADDENDUM);

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(file1));
			BufferedReader bfr1 = new BufferedReader(new FileReader(file2));

			while ((z = bfr1.readLine()) != null)
				s3 += z;

			while ((y = bfr.readLine()) != null)
				s1 += y;
			bfr1.close();
			bfr.close();
		} catch (IOException e) {
			return "Error Reading the output file";
		}

		if (!s3.equals(s1)) {
			return "Output does not match key";
		}
		return "Successful output match";
	}
}

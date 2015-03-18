package APEServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceConverter {

	final static String COMPILE_COMMAND = "javac ";
	final static String RUN_COMMAND = "java ";

	final static String KEY_ADDENDUM = "_key.txt";
	final static String OUTPUT_ADDENDUM = "_output.txt";

	public static void compileAndRun(String code, String name)
			throws IOException, APEException {

		File directory = new File(name);
		if (!directory.exists()) {
			directory.mkdir();
		}
		
		cleanup(directory);

		// Create .java file for compilation
		File dotjava = new File(directory.getPath() + "/" + name + ".java");
		PrintWriter fout = new PrintWriter(dotjava);
		fout.write(code);
		fout.close();

		// Compile .java file
		Process comp_proc = Runtime.getRuntime().exec(
				COMPILE_COMMAND + dotjava.getPath());
		try {
			comp_proc.waitFor();
		} catch (InterruptedException e) {
		}
		if (comp_proc.isAlive())
			throw new APEException("Compiling taking too long");
		if (comp_proc.exitValue() != 0) {
			throw new APEException("Failed to compile source");
		} else
			System.out.println("Compiled Successfully");

		// run generated java program
		Process run_proc = Runtime.getRuntime().exec(
				RUN_COMMAND + " -cp " + directory.getPath() + " " + name);
		BufferedReader proc_out = new BufferedReader(new InputStreamReader(
				run_proc.getInputStream()));
		File output = new File(directory.getPath() + "/" + name
				+ OUTPUT_ADDENDUM);
		try {
			run_proc.waitFor(3000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}
		if (run_proc.isAlive()) {
			run_proc.destroy();
			throw new APEException("Runtime is taking too long");
		}

		if (run_proc.exitValue() != 0) {
			throw new APEException("Error during runtime");
		} else
			System.out.println("Ran Successfully");

		if (!output.exists()) {
			output.createNewFile();
			fout = new PrintWriter(output);
			String s = null;
			while ((s = proc_out.readLine()) != null) {
				fout.write(s);
			}
			fout.close();
		}

		// Compare outputs
		compareOutputFiles(output, name);
	}

	public static String getName(String code) throws APEException {
		// Extract the name for the source file
		Pattern p = Pattern.compile("public class (.*?) ");
		Matcher m = p.matcher(code);
		String name = "DynSource";
		if (!m.find())
			throw new APEException("Could not find class name");

		name = m.group(1);
		System.out.println("Found class name: " + name);
		return name;
	}

	public static void compareOutputFiles(File output, String name)
			throws APEException {
		String s1 = "";
		String s3 = "";
		String y = "", z = "";

		File file1 = new File("keys/" + name + KEY_ADDENDUM);

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(file1));
			BufferedReader bfr1 = new BufferedReader(new FileReader(output));

			while ((z = bfr1.readLine()) != null)
				s3 += z;

			while ((y = bfr.readLine()) != null)
				s1 += y;
			bfr1.close();
			bfr.close();
		} catch (IOException e) {
			throw new APEException("Error reading the output file");
		}

		if (!s3.equals(s1)) {
			throw new APEException("Output does not match key");
		}
	}

	public static void cleanup(File directory) {
		if(!directory.exists())
			return;
		File[] files = directory.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					cleanup(f);
				} else
					f.delete();
			}
		}
	}
}

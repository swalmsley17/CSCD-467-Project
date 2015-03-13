import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;



public class TestOne {
	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter fout = new PrintWriter(new File("TestOne_output.txt"));
		fout.println("Hello World!");
		fout.close();
	}
}

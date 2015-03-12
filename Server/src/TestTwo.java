import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class TestTwo {

	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter fout = new PrintWriter(new File("TestTwo_output.txt"));
		methodOne("this_is_method_one",fout);
		fout.close();
	}
	
	private static void methodOne(String message, PrintWriter fout) {
		fout.println(message);
		methodTwo("this_is_method_two",fout);
	}
	private static void methodTwo(String message, PrintWriter fout) {
		fout.println(message);
	}

}

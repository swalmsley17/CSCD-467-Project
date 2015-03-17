
public class TestTwo {

	public static void main(String[] args) {
		methodOne("this_is_method_one");
	}

	private static void methodOne(String message) {
		System.out.println(message);
		methodTwo("this_is_method_two");
	}

	private static void methodTwo(String message) {
		System.out.println(message);
	}

}

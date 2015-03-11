import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

class SourceJavaFileObject extends SimpleJavaFileObject
{
	final String code;
	SourceJavaFileObject(String name, String code) {
		super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),
				Kind.SOURCE);
		this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}

public class SourceConverter
{
	static class ErrorCallable implements Callable<String>
	{
		private String text;
		public ErrorCallable(String message)
		{
			this.text = message;
		}
		@Override
		public String call() throws Exception {
			return this.text;
		}

	}

	private static boolean compileSource(String name, String text)
	{
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		JavaFileObject file = new SourceJavaFileObject(name, text);
		Iterable<? extends JavaFileObject> compilation_units = Arrays.asList(file);
		Iterable<String> options = Arrays.asList("-d", "bin");

		CompilationTask task = compiler.getTask(null, null, null, options, null, compilation_units);
		return task.call();
	}

	@SuppressWarnings("unchecked")
	public static Callable<String> textToRunnable(String text)
	{
		// Extract the name for the source file
		Pattern p = Pattern.compile("public class (.*?) implements");
		Matcher m = p.matcher(text);
		String name = "DynSource";
		if (!m.find())
			return new ErrorCallable("Could not find class name");
		
		name = m.group(1);
		System.out.println("Found class name: " + name);
		if (!SourceConverter.compileSource(name, text))
			System.out.println("Failed to compile source");

		String input = "Foo bar";
		Callable<String> function = null;
		try {
			function = (Callable<String>) Class.forName(name).getConstructor(String.class).newInstance(input);
		} catch (Exception e) {
			function = new ErrorCallable(e.toString());
		}

		return function;
	}
}

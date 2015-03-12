package APEServer;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

class SourceJavaFileObject extends SimpleJavaFileObject {
	final String code;

	SourceJavaFileObject(String name, String code) {
		super(URI.create("string:///" + name.replace('.', '/')
				+ Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}

public class SourceConverter {
	private static boolean compileSource(String name, String text) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		JavaFileObject file = new SourceJavaFileObject(name, text);
		Iterable<? extends JavaFileObject> compilation_units = Arrays
				.asList(file);
		Iterable<String> options = Arrays.asList("-d", "bin");

		CompilationTask task = compiler.getTask(null, null, null, options,
				null, compilation_units);
		return task.call();
	}

	public static Method[] textToRunnable(String text) throws APEException {
		// Extract the name for the source file
		Pattern p = Pattern.compile("public class (.*?) ");
		Matcher m = p.matcher(text);
		String name = "DynSource";
		if (!m.find())
			throw new APEException("Could not find class name");

		name = m.group(1);
		System.out.println("Found class name: " + name);
		if (!SourceConverter.compileSource(name, text)) {
			throw new APEException("Issue in compiling code");
		}

		Method[] functions = null;
		try {
			Method temp = Class.forName(name).getDeclaredMethod("main",
					String[].class);
			functions = new Method[1];
			functions[0] = temp;
		} catch (NoSuchMethodException e) {
		} catch (Exception e) {
			throw new APEException("Error Getting Declared Method\n"
					+ e.getMessage());
		}

		if (functions == null) {
			try {
				functions = Class.forName(name).getDeclaredMethods();
			} catch (SecurityException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return functions;
	}
}

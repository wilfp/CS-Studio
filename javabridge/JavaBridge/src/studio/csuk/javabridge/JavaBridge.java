package studio.csuk.javabridge;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class JavaBridge {

	private File directory;

	public JavaBridge(File directory) {
		
		// init
		this.directory = directory;
		
		startCLI();
	}

	public void startCLI() {
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {

			if(!sc.hasNext()) continue;

			String next = sc.next();

			if(next.equals(".exit")) break;

			ProgramResult result = runFile(next);
			log("Run output: " + result.getOutput());
			log("Run state: " + result.getState());
			sc.reset();
		}

		sc.close();
		log("Exiting...");
	}
	
	public ProgramResult runFile(String serialName) {

		InjectionLogger.get().register(serialName);

		ProgramResult result = new ProgramResult();

		try {

			File target = new File(directory, serialName + ".java");

			File processed = new File(directory, serialName + "_processed.java");
			processed.createNewFile();

			boolean startedCode = false;
			int lineNumber = 0;
			int bracketCount = 0;

			FileWriter fw = new FileWriter(processed);
			FileReader reader = new FileReader(target);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = null;

			while((line = bufferedReader.readLine()) != null){

				if(!startedCode && !line.contains("{"))
					continue;

				startedCode = true;

				if(line.contains("{")) bracketCount++;

				if(line.contains("}")) bracketCount--;

				if(bracketCount > 1) {
					fw.write(line + "\r\n" + InjectionLogger.get().getLineCode(serialName, lineNumber) + "\r\n");
				}else{
					fw.write(line + "\r\n");
				}

				lineNumber++;
			}

			fw.close();

			CompileResult compileResult = compile(serialName, processed);

			if (compileResult.getState() == CompileResult.State.FAIL) {
				result.setError(compileResult.getError());
				result.setLineNumber(compileResult.getLineNumber());
				result.setState(ProgramResult.State.COMPILE_ERROR);
				return result;
			}

			RunResult runResult = runClass(serialName);

			result.setOutput(runResult.getOutput());
			result.setState(ProgramResult.State.SUCCESS);

			LinkedList<Integer> lines = InjectionLogger.get().getLines(serialName);
			result.setLines(lines);

		}catch(IOException e){
			e.printStackTrace();
		}

		InjectionLogger.get().remove(serialName);

		return result;
	}
	
	private CompileResult compile(String serialName, File file) {

		try {

			File[] files = new File[]{ file };

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

			Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));

			StringWriter sw = new StringWriter();

			boolean called = compiler.getTask(sw, fileManager, null, null, null, compilationUnits1).call();

			fileManager.close();

			if(called){
				File outputFile = new File(directory, serialName + ".class");
				return new CompileResult(CompileResult.State.SUCCESS, "", 0, sw.toString(), outputFile);
			}else{
				return new CompileResult(CompileResult.State.FAIL, "", 0, sw.toString(), null);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private RunResult runClass(String className){

		try {
			URLClassLoader loader = new URLClassLoader(new URL[]{ directory.toURI().toURL() });
			Class c = loader.loadClass(className);

			// Capture System.out data
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(bytes);
			PrintStream systemOut = System.out;
			System.setOut(ps);

			// Run main method
			Method main = c.getMethod("main", String[].class);
			String[] params = new String[]{ null };
			main.invoke(null, new Object[]{ params });

			// Reset to default
			System.out.flush();
			System.setOut(systemOut);

			return new RunResult(RunResult.State.SUCCESS, "", 0, bytes.toString());

		}catch (Exception e){
			e.printStackTrace();
			return new RunResult(RunResult.State.FAIL, e.getMessage(), 0, "");
		}
	}
	
	private static void log(String text) {
		System.out.println(text);
	}
}

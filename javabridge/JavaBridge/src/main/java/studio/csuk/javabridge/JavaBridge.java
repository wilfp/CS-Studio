package studio.csuk.javabridge;

import org.json.JSONObject;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.Scanner;

public class JavaBridge {

	private File processedDir;
	private File temp;

	public JavaBridge(File temp) {

		// The root directory file
		this.temp = temp;

		// folder for processed files
		this.processedDir = new File(this.temp, "/processed/");
		this.processedDir.mkdir();

		// start listening for input
		startCLI();
	}

	public void startCLI() {

		Scanner sc = new Scanner(System.in);

		while(true) {

			if(!sc.hasNext()) continue;

			String next = sc.next();

			if(next.equals(".exit")) break;

			ProgramResult result = runFile(next);
			printResult(next, result);
			sc.reset();
		}

		sc.close();
		log("Exiting...");
	}

	public ProgramResult runFile(String serialName) {

		InjectionLogger.get().register(serialName);

		ProgramResult result = new ProgramResult();

		try {

			File target = new File(temp, serialName + ".java");

			File processed = new File(processedDir, serialName + ".java");
			processed.createNewFile();

			int lineNumber = 1;
			int bracketCount = 0;

			FileWriter fw = new FileWriter(processed);
			FileReader reader = new FileReader(target);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = null;
			boolean nextLineOffsetFlag = false;

			while((line = bufferedReader.readLine()) != null){

				if(line.contains("%NAMEPOINT%")){
					line = line.replace("%NAMEPOINT%", serialName);
				}

				if(line.contains("{")) {
                    bracketCount++;
                    nextLineOffsetFlag = true;
                }

				if(line.contains("}")) {
				    bracketCount--;
				}

				// TODO work out how to include fields in variable tracking

				if(bracketCount > 1) {

				    String all = line + "\r\n";

				    if(nextLineOffsetFlag){
                        all += InjectionLogger.get().getLineCode(serialName, lineNumber, true) + "\r\n";
                    }

				    // TODO if line includes variable
					// TODO regex parsing for declaration, for loop, while loop
					// TODO register here

					// TODO if line includes assignment
					// TODO regex parsing for new value, plus, minus, increment, decrement
					// TODO or alternatively just log new value afterwards
					// TODO log here

				    all += InjectionLogger.get().getLineCode(serialName, lineNumber, false) + "\r\n";

					fw.write(all);

				}else{
					fw.write(line + "\r\n");
				}

				lineNumber++;
                nextLineOffsetFlag = false;
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

			var lines = InjectionLogger.get().getLines(serialName);
			result.setLines(lines);

			// TODO get variable log here
			// TODO set variable log here

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
				File outputFile = new File(processedDir, serialName + ".class");
				return new CompileResult(CompileResult.State.SUCCESS, "", 0, sw.toString(), outputFile);
			}else{
				return new CompileResult(CompileResult.State.FAIL, sw.toString(), 0, null, null);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private RunResult runClass(String className){

		try {
			URLClassLoader loader = new URLClassLoader(new URL[]{ processedDir.toURI().toURL() });
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

	private void log(String text) {
		System.out.println(text);
	}

	private static String base64(String text){

		if(text == null || text.isEmpty()){
			return "";
		}

		return Base64.getEncoder().encodeToString(text.getBytes());
	}

	private void printResult(String serialName, ProgramResult result){

		String json = new JSONObject()
				.put("name", serialName)
				.put("state", result.getState().toString())
				.put("output", base64(result.getOutput()))
				.put("error", base64(result.getError()))
				.put("lines", result.getLines())
				.toString();

		log(json);
    }
}

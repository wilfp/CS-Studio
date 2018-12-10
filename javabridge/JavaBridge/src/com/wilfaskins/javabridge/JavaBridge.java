package com.wilfaskins.javabridge;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

public class JavaBridge {
	
	private static final String FILE_START = "file:";
	
	private File directory;
	
	public JavaBridge(File directory) {
		
		// init
		this.directory = directory;
		
		//startCLI();
	}

	public void startCLI() {
		
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			
			String next = sc.next();
			
			if(next.startsWith(FILE_START)) {
				
				String fileName = next.split(FILE_START)[1].trim();
				runFile(fileName);
				
			}else {
				break;
			}
		}
		
		log("Exit");
		sc.close();
	}
	
	public ProgramResult runFile(String serialName) {

		ProgramResult result = new ProgramResult();

		File target = new File(directory, serialName  + ".java");
		CompileResult compileResult = compile(serialName, target);

		if(compileResult.getState() == CompileResult.State.FAIL){
			result.setError(compileResult.getError());
			result.setLineNumber(compileResult.getLineNumber());
			result.setState(ProgramResult.State.COMPILE_ERROR);
			return result;
		}
		
		RunResult runResult = runClass(compileResult.getFileGenerated());
		
		result.setOutput(runResult.getOutput());
		result.setState(ProgramResult.State.SUCCESS);
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
	
	private RunResult runClass(File file) {
		// https://stackoverflow.com/questions/502218/sandbox-against-malicious-code-in-a-java-application
		return null;
	}
	
	private static void log(String text) {
		System.out.println(text);
	}
}

package com.wilfaskins.javabridge;

import java.io.File;
import java.util.Scanner;

public class JavaBridge {
	
	private static final String FILE_START = "file:";
	
	private File directory;
	
	public JavaBridge(File directory) {
		
		// init
		this.directory = directory;
		
		startCLI();
	}

	private void startCLI() {
		
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
	
	private ProgramResult runFile(String fileName) {

		ProgramResult result = new ProgramResult();

		File target = new File(directory, fileName);
		CompileResult compileResult = compile(target);

		if(compileResult /* .getState() == CompileResult.State.FAIL */){
			result.setError(compileResult /* .getErrorMessage() */);
			result.setLineNumber(compileResult /* .getLineNumber() */);
			result.setState(ProgramResult.State.COMPILE_ERROR);
			return result;
		}
		
		RunResult runResult = runClass(compileResult /* .getFileGenerated */);
		
		result.setOutput(runResult /* .getOutput() */);
		result.setState(ProgramResult.State.SUCCESS);
		return result;
	}
	
	private CompileResult compile(File file) {
		return null;
	}
	
	private RunResult runClass(File file) {
		// https://stackoverflow.com/questions/502218/sandbox-against-malicious-code-in-a-java-application
		return false;
	}
	
	private static void log(String text) {
		System.out.println(text);
	}
}

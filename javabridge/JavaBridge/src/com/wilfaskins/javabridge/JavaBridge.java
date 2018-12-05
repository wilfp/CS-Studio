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
		File classFile = null;
		
		try {
			classFile = compile(target);
		}catch(Exception e) {
			result.setError(e.getLocalizedMessage());
			result.setState(ProgramResult.State.COMPILE_ERROR);
			return result;
		}
		
		
	}
	
	private File compile(File file) {
		return null;
	}
	
	private boolean runClass(File file) {
		return null;
	}
	
	private static void log(String text) {
		System.out.println(text);
	}
}

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
		
		System.out.println("Exiting...");
		sc.close();
	}
	
	private void runFile(String fileName) {
		
		File target = new File(directory, fileName);
		
	}
}

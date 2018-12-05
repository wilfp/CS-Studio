package com.wilfaskins.javabridge;

import java.io.File;

public class JavaBridgeTest {

	public static void main(String[] args) {
		
		File temp = new File("/temp/");
		temp.mkdir();
		
		JavaBridge bridge = new JavaBridge(temp);
		
		temp.delete();
	}

}

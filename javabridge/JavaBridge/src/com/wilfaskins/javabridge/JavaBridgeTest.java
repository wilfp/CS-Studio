package com.wilfaskins.javabridge;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JavaBridgeTest {

	public static void main(String[] args) {

		JavaBridge bridge = new JavaBridge(new File("temp/"));
		bridge.runFile("test");
	}

}

package studio.csuk.javabridge;

import java.io.File;

public class JavaBridgeTest {

	public static void main(String[] args) {

		JavaBridge bridge = new JavaBridge(new File("temp/"));
		ProgramResult result = bridge.runFile("test");

		System.out.println(result.getState());
		System.out.println(result.getOutput());
	}

}

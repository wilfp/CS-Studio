package studio.csuk.javabridge;

import java.io.File;

public class JavaBridgeTest {

    public static void main(String[] args) {

        JavaBridge bridge = new JavaBridge(new File("temp/"));
        bridge.startCLI();
    }

}

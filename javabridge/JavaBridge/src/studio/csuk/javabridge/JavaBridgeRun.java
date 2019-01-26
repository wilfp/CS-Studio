package studio.csuk.javabridge;

import java.io.File;

public class JavaBridgeRun {

    public static void main(String[] args) {

        JavaBridge bridge = new JavaBridge(new File("work/"));
        bridge.startCLI();
    }

}

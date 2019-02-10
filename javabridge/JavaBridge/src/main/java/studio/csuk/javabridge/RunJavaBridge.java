package studio.csuk.javabridge;

import java.io.*;

public class RunJavaBridge {

    public static void main(String[] args) {

        JavaBridge bridge = new JavaBridge(new File(args[0]));
        bridge.startCLI();
    }

}

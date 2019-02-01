package studio.csuk.javabridge;

import java.io.*;

public class RunJavaBridge {

    public static void main(String[] args) {

        File errorLog = new File("javabridge-error-log.txt");

        if(!errorLog.exists()) {
            try {
                errorLog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (PrintStream errorStream = new PrintStream(new FileOutputStream(errorLog))) {
            System.setErr(errorStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JavaBridge bridge = new JavaBridge(new File(args[0]));
        bridge.startCLI();
    }

}

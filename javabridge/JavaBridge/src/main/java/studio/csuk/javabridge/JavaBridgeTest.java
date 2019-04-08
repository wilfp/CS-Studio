package studio.csuk.javabridge;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This class was created to test JavaBridge during development.
 *
 * @author Wilfrid Askins
 */
public class JavaBridgeTest {

    public static void main(String[] args){

        var stream = new ByteArrayInputStream("test".getBytes());
        System.setIn(stream);

        RunJavaBridge.main(new String[]{ "/temp" });
    }

}

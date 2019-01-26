package studio.csuk.javabridge;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class InjectionLogger {

    private Map<String, LinkedList<Integer>> lines;

    public InjectionLogger(){
        this.lines = new HashMap<>();
    }

    public void register(String serialName) {
        lines.put(serialName, new LinkedList<>());
    }

    public LinkedList<Integer> getLines(String serialName){
        return lines.get(serialName);
    }

    public void onLine(String serialName, int line){
        this.lines.get(serialName).add(line);
    }

    public String getLineCode(String serialName, int line){
        return "studio.csuk.javabridge.InjectionLogger.get().onLine(" + serialName + ", " + line + ");";
    }

    private static final InjectionLogger instance = new InjectionLogger();

    public static InjectionLogger get(){
        return instance;
    }
}

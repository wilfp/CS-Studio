package studio.csuk.javabridge;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class InjectionLogger {

    private Map<String, LinkedList<Integer>> lines;
    private Map<String, Integer> runTiming;

    public InjectionLogger(){
        this.lines = new HashMap<>();
        this.runTiming = new HashMap<>();
    }

    public void register(String serialName) {
        lines.put(serialName, new LinkedList<>());
    }

    public void remove(String serialName){
        lines.remove(serialName);
    }

    public LinkedList<Integer> getLines(String serialName){
        return lines.get(serialName);
    }

    @SuppressWarnings("unused")
    public void onLine(String serialName, int line){
        this.lines.get(serialName).add(line);
        this.runTiming.putIfAbsent(serialName, 0);
        this.runTiming.compute(serialName, (s,i) -> i+1);
    }

    public void onVariableInit(String serialName, String variableName, int scope, Object data){

    }


    public String getLineCode(String serialName, int line, boolean offset){

        if(offset) line -= 1;

        return "studio.csuk.javabridge.InjectionLogger.get().onLine(\"" + serialName + "\", " + line + ");";
    }

    public String getVariableInitCode(String serialName, String variableName, int scope, Object data) {

        return String.format("studio.csuk.javabridge.InjectionLogger.get().onVariableInit(%s,%s,%s,%s)", serialName, variableName, scope, data);
    }

    public String getVariableAssignCode(String serialName, int variableID, Object value) {
        return String.format("studio.csuk.javabridge.InjectionLogger.get().onVariableAssign(%s,%s,%s)", serialName, variableID, value);
    }

    private static final InjectionLogger instance = new InjectionLogger();

    public static InjectionLogger get(){
        return instance;
    }
}

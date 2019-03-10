package studio.csuk.javabridge;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class InjectionLogger {

    /** Map of serialName to list of lines executed */
    private Map<String, LinkedList<Integer>> lines;

    /** Map of serialName to time */
    private Map<String, Integer> currentRunTimeMap;
    /** Map of serialName + line count to assignment */
    private Map<String, Map<Integer,Assignment>> assignmentMap;
    /** Map of variableID to variable */
    private Map<Integer,Variable> variableMap;

    public InjectionLogger(){
        this.lines = new HashMap<>();
        this.currentRunTimeMap = new HashMap<>();
    }

    public void register(String serialName) {
        lines.put(serialName, new LinkedList<>());
        currentRunTimeMap.put(serialName, 0);
    }

    public void remove(String serialName){
        lines.remove(serialName);
        currentRunTimeMap.remove(serialName);
        assignmentMap.remove(serialName);
    }

    public LinkedList<Integer> getLines(String serialName){
        return lines.get(serialName);
    }

    @SuppressWarnings("unused")
    public void onLine(String serialName, int line){
        lines.get(serialName).add(line);
        currentRunTimeMap.compute(serialName, (s, i) -> i+1);
    }

    public void onVariableInit(String serialName, String variableName, int scope, Object value){
        // register the variable with the system
    }

    public void onVariableAssign(String serialName, int variableID, Object value){
        // add an entry for the variable at this time with this value
    }

    public String getLineCode(String serialName, int line, boolean offset){

        if(offset) line -= 1;

        return "studio.csuk.javabridge.InjectionLogger.get().onLine(\"" + serialName + "\", " + line + ");";
    }

    public String getVariableInitCode(String serialName, String variableName, int scope, Object value) {

        return String.format("studio.csuk.javabridge.InjectionLogger.get().onVariableInit(%s,%s,%s,%s)", serialName, variableName, scope, value);
    }

    public String getVariableAssignCode(String serialName, int variableID, Object value) {
        return String.format("studio.csuk.javabridge.InjectionLogger.get().onVariableAssign(%s,%s,%s)", serialName, variableID, value);
    }

    class Assignment{
        private int variableID;
        private Object value;
    }

    class Variable{
        private int variableID;
        private int scope;
    }

    private static final InjectionLogger instance = new InjectionLogger();

    public static InjectionLogger get(){
        return instance;
    }
}

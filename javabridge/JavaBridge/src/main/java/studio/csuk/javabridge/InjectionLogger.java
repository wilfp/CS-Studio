package studio.csuk.javabridge;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.*;

public class InjectionLogger {

    /** Map of serialName to list of lines executed */
    private Map<String, List<Integer>> lines;

    /** Map of serialName to time */
    private Map<String, Integer> currentRunTimeMap;
    /** Map of variableID to line + value */
    private Map<Integer, Map<Integer,Object>> assignmentMap;
    /** Map of variableID to variable */
    private Map<Integer,Variable> variableMap;
    /** Map of serialName to variableIDs */
    private Map<String, List<Integer>> serialVariableMap;

    public InjectionLogger(){
        this.lines = new HashMap<>();
        this.currentRunTimeMap = new HashMap<>();
        this.assignmentMap = new HashMap<>();
        this.serialVariableMap = new HashMap<>();
    }

    public void register(String serialName) {
        lines.put(serialName, new LinkedList<>());
        currentRunTimeMap.put(serialName, 0);
        serialVariableMap.put(serialName, new LinkedList<>());
    }

    public void remove(String serialName){

        lines.remove(serialName);
        currentRunTimeMap.remove(serialName);

        serialVariableMap.get(serialName).stream().forEach(id -> assignmentMap.remove(id));
        serialVariableMap.remove(serialName);
    }

    @SuppressWarnings("unused")
    public void onLine(String serialName, int line){
        // add this line to the history of all lines
        lines.get(serialName).add(line);
        // increment the current line by one
        currentRunTimeMap.compute(serialName, (s, i) -> i+1);
    }

    public void onVariableInit(String serialName, String variableName, int scope, Object value){

        // create a new variable instance
        // TODO allocate variable IDs
        var variable = Variable.of(0, serialName, variableName, scope);

        // register the variable with the system
        variableMap.put(variable.getVariableID(), variable);
        assignmentMap.put(variable.getVariableID(), new HashMap<>());
    }

    public void onVariableAssign(String serialName, int variableID, Object value){

        // add an entry for the variable at this time with this value
        // get the assignments map
        var map = assignmentMap.get(variableID);
        // get the current time (in lines) of the program
        var currentTime = currentRunTimeMap.get(serialName);
        // add the new value at the specified time
        map.put(currentTime, value);
    }

    public String getLineCode(String serialName, int line, boolean offset){

        if(offset) line -= 1;

        return "studio.csuk.javabridge.InjectionLogger.get().onLine(\"" + serialName + "\", " + line + ");";
    }

    public String getVariableInitCode(String serialName, int line, String variableName, int scope, Object value, boolean offset) {

        if(offset) line -= 1;

        return String.format("studio.csuk.javabridge.InjectionLogger.get().onVariableInit(%s,%s,%s,%s)", serialName, variableName, scope, value);
    }

    public String getVariableAssignCode(String serialName, int line, int variableID, Object value, boolean offset) {

        if(offset) line -= 1;

        return String.format("studio.csuk.javabridge.InjectionLogger.get().onVariableAssign(%s,%s,%s)", serialName, variableID, value);
    }

    public List<Integer> getLines(String serialName){
        return lines.get(serialName);
    }

    public Map<Integer, Map<Integer,Object>> getAssignments(String serialName){
        // TODO return assignments of this serial
    }


    @Value(staticConstructor="of")
    class Variable{

        private int variableID;
        private String serialName;
        private String variableName;
        private int scope;
    }

    private static final InjectionLogger instance = new InjectionLogger();

    public static InjectionLogger get(){
        return instance;
    }
}

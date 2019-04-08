package studio.csuk.javabridge;

import java.util.LinkedList;
import java.util.List;

public class ProgramResult extends AbstractResult {

	private State state;
	private List<Integer> lines;

	public ProgramResult(){
		super(null,-1,null);
		this.state = null;
	}

	public ProgramResult(State state, String error, int lineNumber, String output, List<Integer> lines) {
		super(error, lineNumber, output);
		this.state = state;
		this.lines = lines;
	}

	public List<Integer> getLines() {
		return lines;
	}

	public void setLines(List<Integer> lines) {
		this.lines = lines;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public enum State{
		SUCCESS, COMPILE_ERROR, RUNTIME_ERROR;
	};
}

package studio.csuk.javabridge;

import java.util.LinkedList;

public class ProgramResult extends AbstractResult {

	private State state;
	private LinkedList<Integer> lines;

	public ProgramResult(){
		super(null,-1,null);
		this.state = null;
	}

	public ProgramResult(State state, String error, int lineNumber, String output, LinkedList<Integer> lines) {
		super(error, lineNumber, output);
		this.state = state;
		this.lines = lines;
	}

	public LinkedList<Integer> getLines() {
		return lines;
	}

	public void setLines(LinkedList<Integer> lines) {
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

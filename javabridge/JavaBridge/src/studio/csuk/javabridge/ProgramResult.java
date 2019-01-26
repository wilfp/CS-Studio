package studio.csuk.javabridge;

public class ProgramResult extends AbstractResult {

	private State state;

	public ProgramResult(){
		super(null,-1,null);
		this.state = null;
	}

	public ProgramResult(State state, String error, int lineNumber, String output) {
		super(error, lineNumber, output);
		this.state = state;
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

package studio.csuk.javabridge;

public class RunResult extends AbstractResult{

    public RunResult(State state, String error, int lineNumber, String output) {
        super(error, lineNumber, output);
        this.state = state;
    }

    private State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State{
        SUCCESS, FAIL;
    };


}

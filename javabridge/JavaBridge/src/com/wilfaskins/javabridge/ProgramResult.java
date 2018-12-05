package com.wilfaskins.javabridge;

public class ProgramResult {

	private State state;
	private String error;
	private int lineNumber;
	private String output;
	
	public ProgramResult() {
		super();
	}
	
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public enum State{
		SUCCESS, COMPILE_ERROR, RUNTIME_ERROR;
	};
}

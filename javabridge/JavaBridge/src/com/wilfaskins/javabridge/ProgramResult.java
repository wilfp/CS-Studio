package com.wilfaskins.javabridge;

public class ProgramResult {

	private State state;
	private String error;
	
	public ProgramResult() {
		super();
	}
	
	public ProgramResult(State state, String error) {
		super();
		this.state = state;
		this.error = error;
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

	public enum State{
		SUCCESS, COMPILE_ERROR, RUNTIME_ERROR;
	};
}

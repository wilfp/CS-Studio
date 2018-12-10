package com.wilfaskins.javabridge;

import java.io.File;

public class CompileResult extends AbstractResult{

    public CompileResult(State state, String error, int lineNumber, String output, File file) {
        super(error, lineNumber, output);
        this.state = state;
        this.file = file;
    }

    private State state;
    private File file;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public File getFileGenerated() {
        return file;
    }

    public enum State{
        SUCCESS, FAIL;
    };

}

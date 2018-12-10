package com.wilfaskins.javabridge;

public abstract class AbstractResult {

    private String error;
    private int lineNumber;
    private String output;

    public AbstractResult(String error, int lineNumber, String output) {
        this.error = error;
        this.lineNumber = lineNumber;
        this.output = output;
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

}

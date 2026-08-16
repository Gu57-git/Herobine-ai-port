package org.jakub1221.herobrineai.AI;

public class CoreResult {
    private boolean result;
    private String message;

    public CoreResult(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean getResult() { return result; }
    public String getMessage() { return message; }
}
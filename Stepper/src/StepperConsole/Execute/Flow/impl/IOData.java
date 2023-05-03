package StepperConsole.Execute.Flow.impl;

/**
 * holds an input/output data from a flow that has been executed.
 */
public class IOData {

    private final boolean isOutput;
    private final String name;
    private final String type;

    private final String content;
    private final String necessity;

    private final String userString;

    public IOData(boolean isOutput, String name, String userString, String type, String content, String necessity) {
        this.isOutput = isOutput;
        this.name = name;
        this.userString = userString;
        this.type = type;
        this.content = content;
        this.necessity = necessity;
    }

    public boolean isOutput() {
        return isOutput;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getNecessity() {
        return necessity;
    }

    public String getUserString() {
        return userString;
    }
}

package StepperConsole.Execute.Flow;

public class IOData {

    boolean isOutput;
    String name;
    String type;

    String content;
    String necessity;

    public IOData(boolean isOutput, String name, String type, String content, String necessity) {
        this.isOutput = isOutput;
        this.name = name;
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
}

package JavaFx.Body.FlowExecution;

public class FreeInputsTableRow {
    private String name;
    private String type;
    private String necessity;
    private String value;

    public FreeInputsTableRow(String name, String type, String necessity) {
        this.name = name;
        this.type = type;
        this.necessity = necessity;
        value = "";
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getNecessity() {
        return necessity;
    }

    public String getValue() {
        return value;
    }
}

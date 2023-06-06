package JavaFx.Body.FlowExecution;

import StepperEngine.DTO.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperEngine.DataDefinitions.Enumeration.ZipEnumerator;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class FreeInputsTableRow {
    private String name;
    private String type;
    private String necessity;
    private String value;


    public FreeInputsTableRow(Input input, String value) {
        this.name = input.getDataName();
        this.type = input.getTypeName();
        this.necessity = input.getNecessity();
        this.value = value;

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

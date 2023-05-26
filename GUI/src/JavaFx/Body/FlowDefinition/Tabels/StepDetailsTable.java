package JavaFx.Body.FlowDefinition.Tabels;

import StepperEngine.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperEngine.FlowDetails.StepDetails.FlowIODetails.Output;
import StepperEngine.FlowDetails.StepDetails.StepDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class StepDetailsTable {
    private String stepName;
    private String isReadOnly;

    private ObservableList<Input> inputs;
    private ObservableList<Output> outputs;

    public StepDetailsTable(StepDetails stepDetails){
        this.stepName=stepDetails.getStepName();
        isReadOnly=stepDetails.isReadOnly()? "Yes": "No";
        inputs= FXCollections.observableList(stepDetails.getInputs());
        outputs=FXCollections.observableList(stepDetails.getOutputs());
    }

    public ObservableList<Input> getInputs() {
        return inputs;
    }
    public ObservableList<Output> getOutputs() {
        return outputs;
    }

    public String getStepName() {
        return stepName;
    }

    public String getIsReadOnly() {
        return isReadOnly;
    }
    @Override
    public String toString() {
        return stepName;
    }
}

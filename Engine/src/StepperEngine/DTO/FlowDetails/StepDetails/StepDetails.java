package StepperEngine.DTO.FlowDetails.StepDetails;

import StepperEngine.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperEngine.FlowDetails.StepDetails.FlowIODetails.Output;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface StepDetails {
    String getStepName();
    boolean isReadOnly();


    List<Input> getInputs();
    List<Output> getOutputs();
}

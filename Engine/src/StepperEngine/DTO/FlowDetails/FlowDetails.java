package StepperEngine.DTO.FlowDetails;

import StepperEngine.DTO.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperEngine.DTO.FlowDetails.StepDetails.FlowIODetails.Output;
import StepperEngine.DTO.FlowDetails.StepDetails.StepDetails;

import java.util.List;

public interface FlowDetails {

    String getFlowName();

    String getFlowDescription();

    List<String> getFormalOutputs();

    boolean isFlowReadOnly();
    String isFlowReadOnlyString();

    List<StepDetails> getSteps();

    List<Input> getFreeInputs();
    int getContinuationNumber();
    List<Output> getOutputs();
    List<String> getStepsNames();
}

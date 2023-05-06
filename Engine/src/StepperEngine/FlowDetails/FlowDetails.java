package StepperEngine.FlowDetails;

import StepperEngine.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperEngine.FlowDetails.StepDetails.FlowIODetails.Output;
import StepperEngine.FlowDetails.StepDetails.StepDetails;

import java.util.List;

public interface FlowDetails {

    String getFlowName();

    String getFlowDescription();

    List<String> getFormalOutputs();

    boolean isFlowReadOnly();

    List<StepDetails> getSteps();

    List<Input> getFreeInputs();

    List<Output> getOutputs();
}

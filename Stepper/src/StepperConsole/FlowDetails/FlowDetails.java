package StepperConsole.FlowDetails;

import StepperConsole.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperConsole.FlowDetails.StepDetails.FlowIODetails.Output;
import StepperConsole.FlowDetails.StepDetails.StepDetails;

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

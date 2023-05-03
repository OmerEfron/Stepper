package StepperConsole.FlowDetails;

import StepperConsole.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperConsole.FlowDetails.StepDetails.FlowIODetails.Output;
import StepperEngine.Flow.api.FlowDefinitionInterface;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import StepperConsole.FlowDetails.StepDetails.StepDetails;
import StepperConsole.FlowDetails.StepDetails.StepDetailsImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FlowDetailsImpl implements FlowDetails {


    private final String flowName;

    private final String flowDescription;
    private final boolean readOnly;

    private final List<String> formalOutputs;

    private final List<StepDetails> steps = new ArrayList<>();
    private final List<Input> freeInputs = new ArrayList<>();

    private final List<Output> outputs = new ArrayList<>();


    public FlowDetailsImpl(FlowDefinitionInterface flow){
        this.flowName = flow.getName();
        this.flowDescription = flow.getDescription();
        this.readOnly = flow.isReadOnlyFlow();
        buildFreeInputsDetails(flow);
        formalOutputs = buildFormalOutputs(flow);
        buildSteps(flow);
        buildOutputs(flow);
    }

    private void buildOutputs(FlowDefinitionInterface flow) {
        for(String outputName: flow.getAllOutputs().keySet()){
            DataDefinitionsDeclaration data = flow.getAllOutputs().get(outputName).getKey();
            outputs.add(new Output(outputName, data.dataDefinition().getName(),
                    flow.getAllOutputs().get(outputName).getValue()));
        }
    }

    private void buildSteps(FlowDefinitionInterface flow) {
        flow.getSteps().forEach(step -> steps.add(new StepDetailsImpl(step)));
    }

    private List<String> buildFormalOutputs(FlowDefinitionInterface flow) {
        final List<String> formalOutputs;
        formalOutputs = Arrays.asList(flow.outputStrings().split(","));
        return formalOutputs;
    }

    private void buildFreeInputsDetails(FlowDefinitionInterface flow) {
        for (Map.Entry<DataDefinitionsDeclaration, List<String>> entry : flow.getFreeInputsWithOptional().entrySet()) {
            DataDefinitionsDeclaration data = entry.getKey();
            freeInputs.add(new Input(
                    data.getAliasName(),
                    data.dataDefinition().getName(),
                    String.valueOf(data.necessity()),
                    entry.getValue()
            ));
        }
    }

    @Override
    public String getFlowName() {
        return flowName;
    }

    @Override
    public String getFlowDescription() {
        return flowDescription;
    }

    @Override
    public List<String> getFormalOutputs() {
        return formalOutputs;
    }

    @Override
    public boolean isFlowReadOnly() {
        return readOnly;
    }

    @Override
    public List<StepDetails> getSteps() {
        return steps;
    }

    @Override
    public List<Input> getFreeInputs() {
        return freeInputs;
    }

    @Override
    public List<Output> getOutputs() {
        return outputs;
    }

}

package StepperEngine.FlowDetails.StepDetails;

import StepperEngine.Flow.api.StepUsageDecleration;
import StepperEngine.FlowDetails.StepDetails.FlowIODetails.Input;
import StepperEngine.FlowDetails.StepDetails.FlowIODetails.Output;
import StepperEngine.Step.api.DataDefinitionsDeclaration;
import javafx.util.Pair;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * holds a step details from a certain flow.
 */
public class StepDetailsImpl implements StepDetails{


    private final String stepName; // its final name.
    private final boolean readOnly;

    private List<Input> inputs=new ArrayList<>();
    private List<Output> outputs=new ArrayList<>();

    public StepDetailsImpl(StepUsageDecleration step){
        stepName = step.getStepFinalName();
        readOnly = step.isReadOnlyStep();
        createInputsList(step);
        createOutputsList(step);
    }
    private void createOutputsList(StepUsageDecleration step) {
        Pair<String,String> stepRelatedData;
        for(DataDefinitionsDeclaration output : step.getStepDefinition().getOutputs()){
            if (step.getOutputDataMap().get(output.getAliasName())==null){
                outputs.add(new Output(output.getAliasName(), output.dataDefinition().getName(), "not connected", "not connected"));
            }else {
                for (Pair<String, String> pair : step.getOutputDataMap().get(output.getAliasName())) {
                    outputs.add(new Output(output.getAliasName(), output.dataDefinition().getName(), pair.getKey(), pair.getValue()));
                }
            }

        }
    }
    private void createInputsList(StepUsageDecleration step) {
        Pair<String,String> stepRelatedData;
        for(DataDefinitionsDeclaration input : step.getStepDefinition().getInputs()){
            stepRelatedData= step.getDataMap().containsKey(input.getName()) ?
                    step.getDataMap().get(input.getName()) : new Pair<>("Free Input","Free Input");
            inputs.add(new Input(input.getAliasName(),input.dataDefinition().getName(), String.valueOf(input.necessity()),
                    stepRelatedData));
        }
    }

    @Override
    public List<Output> getOutputs() {
        return outputs;
    }

    @Override
    public List<Input> getInputs() {
        return inputs;
    }

    @Override
    public String getStepName() {
        return stepName;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }
}

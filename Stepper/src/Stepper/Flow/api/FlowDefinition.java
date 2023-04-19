package Stepper.Flow.api;

import Stepper.Step.api.DataDefinitionsDeclaration;
import Stepper.Step.api.DataNecessity;
import Stepper.Step.api.StepDefinitionInterface;

import java.util.*;

public class FlowDefinition implements FlowDefinitionInterface{

    private final String name;

    private final String description;

    private final List<String> flowOutputs;

    private Set<DataDefinitionsDeclaration> freeInputs;

    private final List<StepUsageDeclerationInterface> steps;

    public FlowDefinition(String name, String description){
        this.name = name;
        this.description = description;
        flowOutputs = new ArrayList<>();
        steps = new ArrayList<>();
        freeInputs = new HashSet<>();
    }
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<StepUsageDeclerationInterface> getSteps() {
        return steps;
    }

    @Override
    public List<String> outputStrings() {
        return null;
    }

    @Override
    public boolean isFlowValid() {
        return false;
    }

    @Override
    public Set<DataDefinitionsDeclaration> getFreeInputs() {
        return freeInputs;
    }

    @Override
    public void addStep(StepUsageDeclerationInterface newStep) {
        boolean found = false;
        for(DataDefinitionsDeclaration input:newStep.getStepDefinition().getInputs()) {
            if (input.necessity().equals(DataNecessity.MANDATORY)) { // if its mandatory,
                for (StepUsageDeclerationInterface step : steps) {
                    if (step.getStepDefinition().getOutputs().contains(input)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    freeInputs.add(input);
                }
            }
        }
        steps.add(newStep);
    }
}

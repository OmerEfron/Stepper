package Stepper.Flow.api;

import Stepper.Step.api.StepDefinitionInterface;

public class StepUsageDeclerationClass implements StepUsageDeclerationInterface{

    private final String name;
    private final boolean skipIfFail;

    private final StepDefinitionInterface stepDefinition;

    public StepUsageDeclerationClass(StepDefinitionInterface stepDefinition){
        skipIfFail = false;
        name = stepDefinition.getName();
        this.stepDefinition = stepDefinition;
    }

    StepUsageDeclerationClass(String name, StepDefinitionInterface stepDefinition, boolean skipIfFail){
        this.name = name;
        this.skipIfFail = skipIfFail;
        this.stepDefinition = stepDefinition;
    }

    StepUsageDeclerationClass(String name, StepDefinitionInterface stepDefinition){
        this.skipIfFail = false;
        this.name = name;
        this.stepDefinition = stepDefinition;
    }
    @Override
    public StepDefinitionInterface getStepDefinition() {
        return stepDefinition;
    }

    @Override
    public String getStepFinalName() {
        return name;
    }

    @Override
    public boolean skipIfFail() {
        return skipIfFail;
    }
}

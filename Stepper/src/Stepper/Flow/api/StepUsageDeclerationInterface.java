package Stepper.Flow.api;

import Stepper.Step.api.StepDefinitionInterface;

public interface StepUsageDeclerationInterface {

    StepDefinitionInterface getStepDefinition();
    String getStepFinalName();
    boolean skipIfFail();
}

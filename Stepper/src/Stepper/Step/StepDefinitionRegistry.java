package Stepper.Step;

import Stepper.Step.api.StepDefinitionInterface;
import Stepper.Step.impl.CollectFilesInFolder;
import Stepper.Step.impl.SpendSomeTimeStep;

public enum StepDefinitionRegistry {
    TIME_TO_SPEND(new SpendSomeTimeStep()),
    FILES_COLLECTOR(new CollectFilesInFolder());

    private StepDefinitionInterface stepDefinition;
    StepDefinitionRegistry(StepDefinitionInterface stepDefinition) {
        this.stepDefinition = stepDefinition;
    }
}

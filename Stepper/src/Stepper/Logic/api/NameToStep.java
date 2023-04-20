package Stepper.Logic.api;

import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.Step.StepDefinitionRegistry;

public interface NameToStep {
    boolean isStepInSystem(String name);
    StepDefinitionRegistry getDataDefinitionRegistry(String name);
}

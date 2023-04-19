package Stepper.Logic.api;

import Stepper.Logic.ReadStepper.TheStepper;

import java.util.List;

public interface StepperDefinitionLogic {

    List<String> validateStepper(TheStepper theStepper);
}

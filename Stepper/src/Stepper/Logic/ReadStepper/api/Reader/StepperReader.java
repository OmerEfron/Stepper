package Stepper.Logic.ReadStepper.api.Reader;

import Stepper.Logic.ReadStepper.TheStepper;

public interface StepperReader {
    TheStepper read(String filePath);
}

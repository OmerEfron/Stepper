package Stepper.ReadStepper.api;

import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import Stepper.ReadStepper.Exception.ReadException;

public interface StepperReader {
    TheStepper read(String filePath)throws ReadException;
}

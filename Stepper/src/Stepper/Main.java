package Stepper;

import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.ReadStepper.Exception.ReadException;
import Stepper.ReadStepper.api.StepperReader;
import Stepper.ReadStepper.impl.StepperReaderFromXml;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;

public class Main {

    public static void main(String[] argv) throws ReadException {
        StepperReader stepperReader = new StepperReaderFromXml();
        TheStepper stStepper = stepperReader.read("C:\\Users\\Gil\\Desktop\\stepper1\\Stepper\\ex1 (2).xml");
        Stepper stepper = null;
        try {
            stepper = new Stepper(stStepper);
            stepper.executeFlows();
        } catch (FlowBuildException e) {
            throw new RuntimeException(e);
        }


    }


}

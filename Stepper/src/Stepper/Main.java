package Stepper;

import Stepper.Flow.api.FlowDefinition;
import Stepper.Flow.api.StepUsageDeclerationClass;
import Stepper.Flow.execute.FlowExecution;
import Stepper.Flow.execute.runner.FlowExecutor;
import Stepper.Logic.ReadStepper.api.Reader.StepperReader;
import Stepper.Logic.ReadStepper.api.Reader.StepperReaderFromXml;
import Stepper.Logic.ReadStepper.TheStepper;
import Stepper.Logic.api.StepperDefinitionLogic;
import Stepper.Logic.impl.StepperDefinitionLogicImpl;
import Stepper.Step.impl.*;

public class Main {

    public static void main(String[] argv){
        StepperReader stepperReader = new StepperReaderFromXml();
        TheStepper stStepper = stepperReader.read("C:\\Users\\Gil\\Desktop\\stepper1\\Stepper\\ex1 (2).xml");
        Stepper stepper = new Stepper(stStepper);


    }


}

package Stepper;

import Stepper.Logic.ReadStepper.api.Reader.StepperReader;
import Stepper.Logic.ReadStepper.api.Reader.StepperReaderFromXml;
import Stepper.Logic.ReadStepper.TheStepper;

public class Main {

    public static void main(String[] argv){
//        FlowDefinition flowDefinition=new FlowDefinition("test","lior Bark ben sharmot");
//        flowDefinition.addStep(new StepUsageDeclerationClass(new SpendSomeTimeStep()));
//        FlowExecution flowExecution= new FlowExecution(flowDefinition,"1");
//        FlowExecutor test=new FlowExecutor();
//        test.executeFlow(flowExecution);
        StepperReader stepperReader = new StepperReaderFromXml();
        TheStepper stStepper = stepperReader.read("C:\\Users\\Gil\\Downloads\\xmltest.xml");

    }


}

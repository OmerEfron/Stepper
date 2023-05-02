package StepperEngine;

import StepperEngine.StepperReader.Exception.ReaderException;
import StepperEngine.StepperReader.api.StepperReader;
import StepperEngine.StepperReader.impl.StepperReaderFromXml;
import StepperEngine.StepperReader.XMLReadClasses.TheStepper;

public class Main {

    public static void main(String[] argv) throws ReaderException {
        StepperReader stepperReader = new StepperReaderFromXml();
        TheStepper stStepper = stepperReader.read("C:\\Users\\Gil\\Desktop\\stepper1\\Stepper\\ex1 (2).xml");
        Stepper stepper = null;

        stepper = new Stepper();
        // stepper.executeFlows();

//        DataDefinitionsDeclaration x = new DataDefinitionDeclarationImpl("Source", "bla bka", DataNecessity.MANDATORY, DataDefinitionRegistry.RELATION_STRING);
//        DataDefinitionsDeclaration y = new DataDefinitionDeclarationImpl("Source", "bla bka", DataNecessity.MANDATORY, DataDefinitionRegistry.RELATION);
//        System.out.println(x.equals(y));
    }


}

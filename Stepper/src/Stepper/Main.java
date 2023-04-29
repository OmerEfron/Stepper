package Stepper;

import Stepper.DataDefinitions.Relation.RelationOfStringRows;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;
import Stepper.DataDefinitions.impl.DataDefinitionRegistry;
import Stepper.DataDefinitions.impl.Relation.SteeperRelation;
import Stepper.DataDefinitions.impl.Relation.StepperRelationString;
import Stepper.Flow.FlowBuildExceptions.FlowBuildException;
import Stepper.ReadStepper.Exception.ReadException;
import Stepper.ReadStepper.api.StepperReader;
import Stepper.ReadStepper.impl.StepperReaderFromXml;
import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import Stepper.Step.api.DataDefinitionDeclarationImpl;
import Stepper.Step.api.DataDefinitionsDeclaration;
import Stepper.Step.api.DataNecessity;

import java.util.ArrayList;

public class Main {

    public static void main(String[] argv) throws ReadException {
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

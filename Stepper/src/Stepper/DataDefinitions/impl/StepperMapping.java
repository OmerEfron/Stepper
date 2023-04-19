package Stepper.DataDefinitions.impl;

import Stepper.DataDefinitions.Mapping.Mapping;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;
import javafx.util.Pair;

public class StepperMapping extends DataDefinitionAbstractClass {
    StepperMapping(){
        super("Mapping", false, Mapping.class);
    }

}

package Stepper.DataDefinitions.impl;

import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperNumber extends DataDefinitionAbstractClass {
    public StepperNumber(){
        super("Number", true, Integer.class);
    }
}

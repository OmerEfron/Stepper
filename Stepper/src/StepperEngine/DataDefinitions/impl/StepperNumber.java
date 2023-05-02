package StepperEngine.DataDefinitions.impl;

import StepperEngine.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperNumber extends DataDefinitionAbstractClass {
    public StepperNumber(){
        super("Number", true, Integer.class);
    }
}

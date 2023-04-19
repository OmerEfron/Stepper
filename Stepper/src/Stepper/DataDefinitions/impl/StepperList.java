package Stepper.DataDefinitions.impl;

import Stepper.DataDefinitions.List.DataDefList;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

import java.util.ArrayList;
import java.util.List;

public class StepperList extends DataDefinitionAbstractClass {
    StepperList(){
        super("List", false, DataDefList.class);
    }

}

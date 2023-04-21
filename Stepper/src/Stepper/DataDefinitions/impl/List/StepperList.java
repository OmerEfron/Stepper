package Stepper.DataDefinitions.impl.List;

import Stepper.DataDefinitions.List.DataDefList;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

import java.util.ArrayList;
import java.util.List;

public class StepperList extends DataDefinitionAbstractClass {
    public StepperList(){
        super("List", false, List.class);
    }

}

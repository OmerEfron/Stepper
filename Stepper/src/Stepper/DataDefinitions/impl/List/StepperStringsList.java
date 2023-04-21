package Stepper.DataDefinitions.impl.List;

import Stepper.DataDefinitions.List.FilesListDataDef;
import Stepper.DataDefinitions.List.StringListDataDef;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperStringsList extends DataDefinitionAbstractClass {
    public StepperStringsList(){
        super("List", false, StringListDataDef.class);
    }
}

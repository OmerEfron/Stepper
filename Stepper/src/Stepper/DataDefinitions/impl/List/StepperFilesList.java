package Stepper.DataDefinitions.impl.List;

import Stepper.DataDefinitions.List.FilesListDataDef;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperFilesList extends DataDefinitionAbstractClass {
    public StepperFilesList(){
        super("List", false, FilesListDataDef.class);
    }
}

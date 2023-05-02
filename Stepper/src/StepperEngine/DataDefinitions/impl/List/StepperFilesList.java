package StepperEngine.DataDefinitions.impl.List;

import StepperEngine.DataDefinitions.List.FilesListDataDef;
import StepperEngine.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperFilesList extends DataDefinitionAbstractClass {
    public StepperFilesList(){
        super("List", false, FilesListDataDef.class);
    }
}

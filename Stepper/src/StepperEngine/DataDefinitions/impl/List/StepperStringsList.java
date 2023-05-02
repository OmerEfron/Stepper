package StepperEngine.DataDefinitions.impl.List;

import StepperEngine.DataDefinitions.List.StringListDataDef;
import StepperEngine.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperStringsList extends DataDefinitionAbstractClass {
    public StepperStringsList(){
        super("List", false, StringListDataDef.class);
    }
}

package StepperEngine.DataDefinitions.impl.Mapping;

import StepperEngine.DataDefinitions.Mapping.NumberMapping;
import StepperEngine.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperNumberMapping extends DataDefinitionAbstractClass {
    public StepperNumberMapping() {
        super("Mapping", false, NumberMapping.class);
    }
}

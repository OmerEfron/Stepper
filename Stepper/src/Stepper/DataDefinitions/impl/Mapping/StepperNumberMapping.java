package Stepper.DataDefinitions.impl.Mapping;

import Stepper.DataDefinitions.Mapping.Mapping;
import Stepper.DataDefinitions.Mapping.NumberMapping;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperNumberMapping extends DataDefinitionAbstractClass {
    public StepperNumberMapping() {
        super("Mapping", false, NumberMapping.class);
    }
}

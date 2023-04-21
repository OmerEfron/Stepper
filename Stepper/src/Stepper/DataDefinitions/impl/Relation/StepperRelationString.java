package Stepper.DataDefinitions.impl.Relation;

import Stepper.DataDefinitions.Relation.RelationOfStringRows;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperRelationString extends DataDefinitionAbstractClass {
    public StepperRelationString(){
        super("RelationOfStringRows", false, RelationOfStringRows.class);
    }
}

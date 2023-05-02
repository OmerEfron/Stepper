package StepperEngine.DataDefinitions.impl.Relation;

import StepperEngine.DataDefinitions.Relation.RelationOfStringRows;
import StepperEngine.DataDefinitions.api.DataDefinitionAbstractClass;

public class StepperRelationString extends DataDefinitionAbstractClass {
    public StepperRelationString(){
        super("RelationOfStringRows", false, RelationOfStringRows.class);
    }


}

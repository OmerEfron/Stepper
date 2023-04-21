package Stepper.DataDefinitions.impl.Relation;

import Stepper.DataDefinitions.Relation.Relation;
import Stepper.DataDefinitions.Relation.RelationOfStringRows;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

public class SteeperRelation extends DataDefinitionAbstractClass {
    public SteeperRelation(){
        super("Relation", false, Relation.class);
    }
}

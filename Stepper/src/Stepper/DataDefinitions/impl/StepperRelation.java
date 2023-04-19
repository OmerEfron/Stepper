package Stepper.DataDefinitions.impl;

import Stepper.DataDefinitions.Relation.Relation;
import Stepper.DataDefinitions.api.DataDefinitionAbstractClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StepperRelation extends DataDefinitionAbstractClass {
    StepperRelation(){
        super("Relation", false, Relation.class);
    }
}

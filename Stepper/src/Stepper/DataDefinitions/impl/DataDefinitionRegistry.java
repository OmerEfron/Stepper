package Stepper.DataDefinitions.impl;

import Stepper.DataDefinitions.api.DataDefinitionInterface;
import Stepper.DataDefinitions.impl.List.StepperFilesList;
import Stepper.DataDefinitions.impl.List.StepperList;
import Stepper.DataDefinitions.impl.List.StepperStringsList;
import Stepper.DataDefinitions.impl.Mapping.StepperMapping;
import Stepper.DataDefinitions.impl.Mapping.StepperNumberMapping;
import Stepper.DataDefinitions.impl.Relation.SteeperRelation;
import Stepper.DataDefinitions.impl.Relation.StepperRelationString;

public enum DataDefinitionRegistry implements DataDefinitionInterface {
    STRING(new StepperString()),
    DOUBLE(new StepperDouble()),
    FILE(new StepperFile()),
    LIST(new StepperList()),
    FIELS_LIST(new StepperFilesList()),
    STRINGS_LIST(new StepperStringsList()),
    NUMBER(new StepperNumber()),
    MAPPING(new StepperMapping()),
    NUMBER_MAPPING(new StepperNumberMapping()),
    RELATION(new SteeperRelation()),
    RELATION_STRING(new StepperRelationString());

    DataDefinitionRegistry(DataDefinitionInterface dataDefinitionInterface){
        this.dataDefinitionInterface=dataDefinitionInterface;
    }
    private final DataDefinitionInterface dataDefinitionInterface;

    @Override
    public boolean isUserFriendly() {
        return dataDefinitionInterface.isUserFriendly();
    }

    @Override
    public String getName() {
        return dataDefinitionInterface.getName();
    }

    @Override
    public Class<?> getType() {
        return dataDefinitionInterface.getType();
    }
}

package StepperEngine.DataDefinitions.impl;

import StepperEngine.DataDefinitions.api.DataDefinitionInterface;
import StepperEngine.DataDefinitions.impl.List.StepperFilesList;
import StepperEngine.DataDefinitions.impl.List.StepperList;
import StepperEngine.DataDefinitions.impl.List.StepperStringsList;
import StepperEngine.DataDefinitions.impl.Mapping.StepperMapping;
import StepperEngine.DataDefinitions.impl.Mapping.StepperNumberMapping;
import StepperEngine.DataDefinitions.impl.Relation.SteeperRelation;
import StepperEngine.DataDefinitions.impl.Relation.StepperRelationString;

public enum DataDefinitionRegistry implements DataDefinitionInterface {
    STRING(new StepperString()),
    DOUBLE(new StepperDouble()),
    FILE(new StepperFile()),
    LIST(new StepperList()),
    FILES_LIST(new StepperFilesList()),
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
